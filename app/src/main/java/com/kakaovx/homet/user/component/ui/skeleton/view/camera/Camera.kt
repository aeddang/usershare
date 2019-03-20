package com.kakaovx.homet.user.component.ui.skeleton.view.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.annotation.CallSuper
import androidx.fragment.app.FragmentActivity
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.lib.page.PageRequestPermission
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxFrameLayout
import java.io.File
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import android.hardware.camera2.CameraCharacteristics
import android.view.ViewTreeObserver


abstract class Camera : RxFrameLayout, PageRequestPermission {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)
    companion object {
        private  var viewModelInstance: CameraViewModel? = null
        fun getViewmodel(): CameraViewModel {
            if(viewModelInstance == null) viewModelInstance = CameraViewModel()
            return viewModelInstance!!
        }
    }

    enum class State {
        Preview,
        WaitingLock ,
        WaitingPrecapture,
        WaitingNonePrecapture,
        PictureTaken
    }
    enum class Error{
        CameraDevice,
        CameraState,
        CameraAccess,
        CaptureSession
    }

    enum class PermissionGranted{
        UnChecked,
        Granted,
        Denied
    }

    enum class CaptureMode{
        Image,
        Extraction,
        All
    }

    private val TAG = javaClass.simpleName

    private var viewModel:CameraViewModel = Camera.getViewmodel()
    private var textureView: AutoFitTextureView? = null
    private var blankSurfaceTexture = SurfaceTexture(10)
    private var currentCameraDevice: CameraDevice? = null
    private var state: State = State.Preview
    private var sensorOrientation: Int = 0
    private var deviceOrientation:Int = 0
    private var displayRotation:Int = 0
    private var backgroundExecutor: CameraExecutor? = null
    private var imageReader: ImageReader? = null
    private var extractionReader: ImageReader? = null
    private val cameraOpenCloseLock = Semaphore(1)
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var previewRequest: CaptureRequest? = null
    private var cameraId: String? = null
    private var captureSession: CameraCaptureSession? = null
    private var previewSize: Size? = null
    private var isInit:Boolean = false
    private var captureCompletedRunnable: Runnable = Runnable { onCapture() }
    private var initCameraRunnable: Runnable = Runnable { initCamera() }
    private var releaseCameraRunnable: Runnable = Runnable { releaseCamera() }

    protected open var file: File? = null
    protected open var captureMode:CaptureMode = CaptureMode.Image
    protected open var extractionFps: Int = 15
    protected open var isFront: Boolean = viewModel.isFront
    protected open var isFlash: Boolean = viewModel.isFlash
    protected open var maxPreviewWidth = 1920
    protected open var maxPreviewHeight = 1080


    abstract fun getTextureView(): AutoFitTextureView
    abstract fun getActivity(): FragmentActivity
    abstract fun getNeededPermissions():Array<String>
    protected open fun requestCameraPermission(){ PagePresenter.getInstance<Any>().requestPermission(getNeededPermissions(), this) }
    protected open fun hasCameraPermission():Boolean{ return PagePresenter.getInstance<Any>().hasPermissions(getNeededPermissions() ) }

    @CallSuper
    override fun onRequestPermissionResult(resultAll: Boolean, permissions: List<Boolean>?) {
        viewModel.permissionGranted =  if( resultAll ) PermissionGranted.Granted else PermissionGranted.Denied
    }

    @CallSuper
    override fun onCreated() {
    }

    @CallSuper
    override fun onDestroyed() {
        releaseCamera()
        file?.delete()
        file = null
    }

    @CallSuper
    open fun onInit(){
        viewModel.resetPermissionGranted()
        Handler().post(initCameraRunnable)
    }

    fun takePicture() {
        if( captureMode == CaptureMode.Extraction) return
        lockFocus()
    }

    fun setFrontCamera() {
        viewModel.isFront = true
        if( isFront ) return
        isFront = true
        resetCamera()
    }

    fun setBackCamera() {
        viewModel.isFront = false
        if( !isFront ) return
        isFront = false
        resetCamera()
    }

    fun setUseFlash(flash:Boolean) {
        if( !viewModel.flashSupported ) return
        isFlash = flash
        viewModel.isFlash = flash
    }

    fun toggleCamera() {
        isFront = !isFront
        viewModel.isFront = isFront
        resetCamera()
    }

    fun resetCamera(){
        val handler = Handler()
        handler.post(releaseCameraRunnable)
        handler.post(initCameraRunnable)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if(newConfig.orientation != deviceOrientation) {
            deviceOrientation = newConfig.orientation
            textureView?.let {
                it.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        it.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        resetCamera()
                    }
                })
            }
        }
    }

    @CallSuper
    open fun onPause(){ releaseCamera() }
    @CallSuper
    open fun onResume() { if(viewModel.permissionGranted != PermissionGranted.Denied) initCamera() }

    open fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
    open fun onError(type:Error, data:Any? = null){ Log.d(TAG, type.toString()) }
    open fun onCapture(){}
    open fun onExtraction(image:Image){}
    open fun onCapture(image:Image){}

    private fun initCamera() {
        if(isInit) return
        isInit = true
        textureView = getTextureView()
        startBackgroundThread()
        textureView?.let {
            if (it.isAvailable) {
                openCamera(this.width, this.height)
            } else {
                it.surfaceTextureListener = surfaceTextureListener
            }
        }

    }

    private fun releaseCamera() {
        if(!isInit) return
        isInit = false
        closeCamera()
        stopBackgroundThread()
        textureView = null
    }

    private fun createImageReader( videoWidth:Int, videoHeight:Int) {
        if(captureMode != CaptureMode.Extraction) {
            imageReader = ImageReader.newInstance(videoWidth, videoHeight, ImageFormat.JPEG, 2)
            imageReader?.setOnImageAvailableListener(onImageAvailableListener, backgroundExecutor?.backgroundHandler)
        }
        if(captureMode != CaptureMode.Image) {
            extractionReader = ImageReader.newInstance(videoWidth, videoHeight, ImageFormat.YUV_420_888, extractionFps)
            extractionReader?.setOnImageAvailableListener(onExtractionAvailableListener, backgroundExecutor?.backgroundHandler)
        }
    }

    private fun findCamera(): CameraCharacteristics? {
        val manager = getActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var characteristics :CameraCharacteristics?
        for (cId in manager.cameraIdList) {
            characteristics = manager.getCameraCharacteristics(cId)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT && !isFront) continue
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK && isFront) continue
            characteristics.get( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: continue
            cameraId = cId
            return characteristics
        }
        return null
    }


    private fun setUpCameraOutputs(width: Int, height: Int) {
        val activity = getActivity()
        try {
            val characteristics = findCamera()
            val ratio = width.toFloat()/ height.toFloat()
            characteristics?.let {
                val map = characteristics.get( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                val largest = Collections.max(
                    Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)),
                    CameraUtil.CompareRatioByArea( ratio)
                )
                createImageReader(largest.width, largest.height)
                displayRotation = activity.windowManager.defaultDisplay.rotation
                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                var swappedDimensions = false
                when (displayRotation) {
                    Surface.ROTATION_0, Surface.ROTATION_180 -> if (sensorOrientation == 90 || sensorOrientation == 270) swappedDimensions = true
                    Surface.ROTATION_90, Surface.ROTATION_270 -> if (sensorOrientation == 0 || sensorOrientation == 180) swappedDimensions = true
                    else -> Log.e(TAG, "Display rotation is invalid: $displayRotation")
                }
                val displaySize = Point()
                activity.windowManager.defaultDisplay.getSize(displaySize)
                var rotatedPreviewWidth = width
                var rotatedPreviewHeight = height
                var maxWidth = displaySize.x
                var maxHeight = displaySize.y
                if (swappedDimensions) {
                    rotatedPreviewWidth = height
                    rotatedPreviewHeight = width
                    maxWidth = displaySize.y
                    maxHeight = displaySize.x
                }
                if (maxWidth > maxPreviewWidth ) maxWidth = maxPreviewWidth
                if (maxHeight > maxPreviewHeight ) maxHeight = maxPreviewHeight
                previewSize = CameraUtil.chooseOptimalSize(
                    map.getOutputSizes(SurfaceTexture::class.java),
                    rotatedPreviewWidth, rotatedPreviewHeight,
                    maxWidth, maxHeight,
                    largest
                )
                val orientation = resources.configuration.orientation
                previewSize?.let {
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) textureView?.setAspectRatio( it.width, it.height )
                    else textureView?.setAspectRatio( it.height, it.width )
                }
                val available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                if( available == true ){
                    viewModel.flashSupported = true
                    isFlash = viewModel.isFlash
                } else {
                    viewModel.flashSupported = false
                    isFlash = false
                }
            }

        } catch (e: CameraAccessException) {
            e.printStackTrace()
            onError(Error.CameraAccess)
        } catch (e: NullPointerException) {
            onError(Error.CameraDevice)
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(width: Int, height: Int) {
        if ( !hasCameraPermission() ) {
            requestCameraPermission()
            return
        }
        setUpCameraOutputs(width, height)
        configureTransform(width, height)
        val activity = getActivity()
        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            manager.openCamera(cameraId!!, stateCallback, backgroundExecutor?.backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }

    }

    private fun getOrientation(): Int {
        return (viewModel.ORIENTATIONS.get(displayRotation) + sensorOrientation + 90) % 360
    }

    private fun unlockFocus() {
        try {
            Log.d(TAG, "unlockFocus" )
            previewRequestBuilder?.let { requestBuilder ->
                requestBuilder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL
                )
                setAutoFlash(requestBuilder)
                captureSession?.capture( requestBuilder.build(), captureCallback, backgroundExecutor?.backgroundHandler)
                state = State.Preview
                previewRequest?.let {
                    captureSession?.setRepeatingRequest( it, captureCallback, backgroundExecutor?.backgroundHandler)
                }
            }

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            currentCameraDevice?.close()
            currentCameraDevice = null
            imageReader?.close()
            imageReader = null
            extractionReader?.close()
            extractionReader = null

        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    private fun startBackgroundThread() {
        backgroundExecutor = CameraExecutor()
    }

    private fun stopBackgroundThread() {
        backgroundExecutor?.shutdown()
        backgroundExecutor = null
    }

    private fun createCameraPreviewSession() {
        Log.d(TAG, "createCameraPreviewSession" )
        try {
            val texture = if(textureView != null) textureView!!.surfaceTexture else blankSurfaceTexture
            previewSize?.let { texture.setDefaultBufferSize(it.width, it.height) }
            val surface = Surface(texture)

            previewRequestBuilder = currentCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder?.let { requestBuilder ->
                val output = ArrayList<Surface>()
                output.add(surface)
                imageReader?.let { reader -> output.add(reader.surface) }
                extractionReader?.let { reader ->
                    requestBuilder.addTarget(reader.surface)
                    output.add(reader.surface)
                }
                requestBuilder.addTarget(surface)
                currentCameraDevice?.createCaptureSession( output,
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            captureSession = cameraCaptureSession
                            try {
                                requestBuilder.set( CaptureRequest.CONTROL_AF_MODE,  CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                setAutoFlash( requestBuilder)
                                previewRequest =  requestBuilder.build()
                                previewRequest?.let { captureSession?.setRepeatingRequest( it, captureCallback, backgroundExecutor?.backgroundHandler ) }
                            } catch (e: CameraAccessException) {
                                e.printStackTrace()
                            }
                        }

                        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                            onError(Error.CaptureSession)
                        }
                    }, null
                )
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        previewSize?.let {
            val activity = getActivity()
            val rotation = activity.windowManager.defaultDisplay.rotation
            val matrix = Matrix()
            val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
            val bufferRect = RectF(0f, 0f, it.height.toFloat(), it.width.toFloat())
            val centerX = viewRect.centerX()
            val centerY = viewRect.centerY()
            if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
                matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
                val scale = Math.max(
                    viewHeight.toFloat() / it.height,
                    viewWidth.toFloat() / it.width
                )
                matrix.postScale(scale, scale, centerX, centerY)
                matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
            } else if (Surface.ROTATION_180 == rotation) {
                matrix.postRotate(180f, centerX, centerY)
            }
            textureView?.setTransform(matrix)
        }
    }

    private fun lockFocus() {
        Log.d(TAG, "lockFocus" )
        try {
            previewRequestBuilder?.let {
                it.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START
                )
                state = State.WaitingLock
                captureSession?.capture( it.build(), captureCallback, backgroundExecutor?.backgroundHandler )
            }

        } catch (e: CameraAccessException) {
            e.printStackTrace()
            onError(Error.CameraAccess)
        }
    }

    private fun runPrecaptureSequence() {
        try {
            previewRequestBuilder?.let {
                it.set(
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START
                )
                state = State.WaitingPrecapture
                captureSession?.capture( it.build(), captureCallback, backgroundExecutor?.backgroundHandler)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            onError(Error.CameraAccess)
        }
    }


    private fun captureStillPicture() {
        Log.d(TAG, "captureStillPicture" )
        try {
            currentCameraDevice?.let {
                val captureBuilder = it.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                imageReader?.let { reader-> captureBuilder.addTarget(reader.surface) }
                captureBuilder.set( CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                setAutoFlash(captureBuilder)
                //val rotate = getOrientation()
                //captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotate)
                val sessionCaptureCallback = object : CameraCaptureSession.CaptureCallback() {
                    override fun onCaptureCompleted( session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                        unlockFocus()
                        Handler(Looper.getMainLooper()).post( captureCompletedRunnable )
                    }
                }
                captureSession?.let{ session ->
                    session.stopRepeating()
                    session.abortCaptures()
                    session.capture(captureBuilder.build(), sessionCaptureCallback, null)
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            onError(Error.CameraAccess)
        }
    }

    private fun setAutoFlash(requestBuilder: CaptureRequest.Builder?) {
        if (isFlash) requestBuilder?.set( CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera(width, height)
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
            return true
        }
        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {
            //onSurfaceTextureUpdated( texture )
        }
    }


    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        private fun process(result: CaptureResult) {
            when (state) {
                State.Preview -> { }
                State.WaitingLock -> {
                    val afState = result.get(CaptureResult.CONTROL_AF_STATE)
                    if (afState == null) {
                        captureStillPicture()
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            state = State.PictureTaken
                            captureStillPicture()
                        } else {
                            runPrecaptureSequence()
                        }
                    }
                }
                State.WaitingPrecapture -> {
                    val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                    if (aeState == null ||
                        aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                        aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED
                    ) state = State.WaitingNonePrecapture
                }
                State.WaitingNonePrecapture -> {
                    val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        state = State.PictureTaken
                        captureStillPicture()
                    }
                }
                else -> {}
            }
        }
        override fun onCaptureProgressed( session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult ) {
            process(partialResult)
        }
        override fun onCaptureCompleted( session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            process(result)
        }
    }

    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened( cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            currentCameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
            currentCameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
            currentCameraDevice = null
            onError(Error.CameraState)
        }
    }

    private val onImageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        backgroundExecutor?.execute {
            val image = reader.acquireNextImage()
            if( file != null) {
                backgroundExecutor?.backgroundHandler?.post( CameraUtil.ImageSaver(image, file!!) )
            } else {
                onCapture(image)
                image.close()
            }
        }

    }

    private val onExtractionAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        backgroundExecutor?.execute {
            val image = reader.acquireNextImage()
            onExtraction( image )
            image.close()
        }
    }
}