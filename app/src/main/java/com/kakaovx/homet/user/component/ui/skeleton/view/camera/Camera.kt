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
import android.util.SparseIntArray
import android.view.ViewTreeObserver
import com.kakaovx.homet.user.component.ui.skeleton.thread.HandlerExecutor
import com.kakaovx.homet.user.component.ui.skeleton.view.AutoFitTextureView


abstract class Camera : RxFrameLayout, PageRequestPermission {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)
    companion object {
        private  var viewModelInstance: CameraViewModel? = null
        fun getViewmodel(): CameraViewModel {
            if(viewModelInstance == null) viewModelInstance = CameraViewModel()
            return viewModelInstance!!
        }

        val ORIENTATIONS: SparseIntArray = object : SparseIntArray() {
            init {
                append(Surface.ROTATION_0, 90)
                append(Surface.ROTATION_90, 0)
                append(Surface.ROTATION_180, 270)
                append(Surface.ROTATION_270, 180)
            }
        }

    }

    private var delegate: Delegate? = null
    interface Delegate{
        fun onCaptureStart(camera: Camera){}
        fun onCaptureCompleted(camera: Camera, file:File?){}
        fun onExtractStart(camera: Camera){}
        fun onExtractEnd(camera: Camera){}
        fun onError(camera: Camera, type:Error, data:Any?){}
    }
    fun setOnCameraListener( _delegate:Delegate? ){ delegate = _delegate }

    val myArray: SparseIntArray = object : SparseIntArray() {
        init {
            append(1, 2)
            append(10, 20)
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
    enum class CameraRatioType{
        Largest,
        Smallest,
        LargestViewRatio,
        SmallestViewRatio,
        Custom
    }

    private val TAG = javaClass.simpleName

    private var viewModel:CameraViewModel = Camera.getViewmodel()
    private var textureView: AutoFitTextureView? = null
    private var blankSurfaceTexture = SurfaceTexture(10)
    private var currentCameraDevice: CameraDevice? = null
    private var backgroundExecutor: HandlerExecutor? = null
    private var imageReader: ImageReader? = null
    private var extractionReader: ImageReader? = null
    private val cameraOpenCloseLock = Semaphore(1)
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var previewRequest: CaptureRequest? = null
    private var captureSession: CameraCaptureSession? = null
    private var isInit:Boolean = false
    private var captureCompletedRunnable: Runnable = Runnable { onCapture() }
    private var initCameraRunnable: Runnable = Runnable { initCamera() }
    private var releaseCameraRunnable: Runnable = Runnable { releaseCamera() }


    var previewSize: Size? = null; private set
    var cameraOutputSize:Size? = null; private set
    var cameraId: String? = null; private set
    var state: State = State.Preview; private set
    var sensorOrientation: Int = 0; private set
    var deviceOrientation:Int = 0; private set
    var displayRotation:Int = 0; private set
    var isFront: Boolean = viewModel.isFront; protected set
    var isFlash: Boolean = viewModel.isFlash; protected set

    protected open var cameraRatioType:CameraRatioType = CameraRatioType.Largest
    protected open var file: File? = null
    protected open var captureMode:CaptureMode = CaptureMode.Image
    protected open var extractionFps: Int = 15
    protected open var maxPreviewWidth = 1920
    protected open var maxPreviewHeight = 1080

    var customSize: Size = Size(maxPreviewWidth,maxPreviewHeight)

    protected open fun getTextureView(): AutoFitTextureView {
        val texture = AutoFitTextureView(context)
        this.addView(texture,0, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        return texture
    }

    abstract fun getActivity(): FragmentActivity?
    abstract fun getNeededPermissions():Array<String>
    protected open fun requestCameraPermission(){ PagePresenter.getInstance<Any>().requestPermission(getNeededPermissions(), this) }
    protected open fun hasCameraPermission():Boolean{ return PagePresenter.getInstance<Any>().hasPermissions(getNeededPermissions() ) }

    @CallSuper
    override fun onRequestPermissionResult(resultAll: Boolean, permissions: List<Boolean>?) {
        viewModel.permissionGranted =  if( resultAll ) PermissionGranted.Granted else PermissionGranted.Denied
    }

    @CallSuper
    override fun onCreatedView() {
    }

    @CallSuper
    override fun onDestroyedView() {
        releaseCamera()
        delegate = null
        file?.delete()
        file = null
        Log.d(TAG, "onDestroyedView")
    }

    @CallSuper
    open fun startCamera(){
        getActivity()?.let {
            viewModel.resetPermissionGranted()
            deviceOrientation= it.windowManager.defaultDisplay.rotation
            Handler().post(initCameraRunnable)
        }
    }

    fun takePicture() {
        if( captureMode == CaptureMode.Extraction) return
        delegate?.onCaptureStart(this)
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
                        textureViewAspectRatio()
                    }
                })
            }

        }
    }

    @CallSuper
    open fun onPause(){ releaseCamera() }
    @CallSuper
    open fun onResume() { if(viewModel.permissionGranted != PermissionGranted.Denied) initCamera() }

    open fun onTextureViewUpdated(texture: SurfaceTexture) {}
    open fun onPreview(){}
    open fun onExtract(image:Image){}
    open fun onCapture(image:Image){}
    open fun onCapture(){
        delegate?.onCaptureCompleted(this, file)
    }

    @CallSuper
    open fun onError(type:Error, data:Any? = null){
        delegate?.onError(this, type, data )
    }



    private fun initCamera() {
        if(isInit) return
        isInit = true
        textureView = getTextureView()
        startBackgroundThread()
        textureView?.let { texture ->
            if (texture .isAvailable) {
                openCamera(width, height)
            } else {
                texture .surfaceTextureListener = surfaceTextureListener
            }
        }
    }

    private fun releaseCamera() {
        if(!isInit) return
        isInit = false
        closeCamera()
        stopBackgroundThread()
        textureView?.let { removeView(it) }
        textureView = null
        cameraOutputSize = null
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
        getActivity()?.let { activity->
            val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
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
        }
        return null
    }


    private fun setUpCameraOutputs(width: Int, height: Int) {

        try {
            val activity = getActivity()
            val characteristics = findCamera()
            characteristics?.let { character ->
                activity?.let { displayRotation = it.windowManager.defaultDisplay.rotation }
                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

                Log.d(TAG, "sensorOrientation $sensorOrientation")
                var swappedDimensions = false
                when (displayRotation) {
                    Surface.ROTATION_0, Surface.ROTATION_180 -> if (sensorOrientation == 90 || sensorOrientation == 270) swappedDimensions = true
                    Surface.ROTATION_90, Surface.ROTATION_270 -> if (sensorOrientation == 0 || sensorOrientation == 180) swappedDimensions = true
                    else -> Log.e(TAG, "Display rotation is invalid: $displayRotation")
                }
                val displaySize = Point()
                activity?.windowManager?.defaultDisplay?.getSize(displaySize)

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

                val map = character.get( CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                val outputs = map.getOutputSizes(ImageFormat.JPEG)
                cameraOutputSize = when (cameraRatioType){
                    CameraRatioType.Largest -> Collections.max(Arrays.asList(*outputs), CameraUtil.CompareSizesByArea())
                    CameraRatioType.Smallest -> Collections.min(Arrays.asList(*outputs), CameraUtil.CompareSizesByArea())
                    CameraRatioType.LargestViewRatio -> {
                        val ratio = rotatedPreviewWidth.toFloat()/ rotatedPreviewHeight.toFloat()
                        Collections.min( Arrays.asList(*outputs), CameraUtil.CompareRatioByArea( ratio))
                    }
                    CameraRatioType.SmallestViewRatio -> {
                        val ratio = rotatedPreviewWidth.toFloat()/ rotatedPreviewHeight.toFloat()
                        Collections.min( Arrays.asList(*outputs.reversedArray()), CameraUtil.CompareRatioByArea( ratio))
                    }
                    CameraRatioType.Custom -> {
                        Collections.min( Arrays.asList(*outputs.reversedArray()), CameraUtil.CompareByArea( customSize ))
                    }
                }

                Log.d(TAG, "cameraOutputSize $cameraOutputSize")
                createImageReader(cameraOutputSize!!.width, cameraOutputSize!!.height)

                if (maxWidth > maxPreviewWidth ) maxWidth = maxPreviewWidth
                if (maxHeight > maxPreviewHeight ) maxHeight = maxPreviewHeight
                previewSize = CameraUtil.chooseOptimalSize(
                    map.getOutputSizes(SurfaceTexture::class.java),
                    rotatedPreviewWidth, rotatedPreviewHeight,
                    maxWidth, maxHeight,
                    cameraOutputSize!!
                )

                Log.d(TAG, "previewSize $previewSize")
                this.textureViewAspectRatio()
                val available = character.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
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

    private fun textureViewAspectRatio(){
        val orientation = resources.configuration.orientation
        previewSize?.let {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) textureView?.setAspectRatio( it.width, it.height )
            else textureView?.setAspectRatio( it.height, it.width )
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
        val manager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            cameraId?.let { manager.openCamera(it, stateCallback, backgroundExecutor?.backgroundHandler) }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }

    }

    fun getOrientation(): Int {
        getActivity()?.let {
            val rotation = it.windowManager.defaultDisplay.rotation
            var rotate = ORIENTATIONS.get(rotation) + ((sensorOrientation-90) %180)
            if (isFront && rotation == Surface.ROTATION_0) rotate = -rotate

            return rotate
        }
        return 0
    }

    fun isSwap(): Boolean {
        if( !isFront ) return false
        if( ((sensorOrientation-90) %180) == 0) return true
        return false
    }

    private fun unlockFocus() {
        try {
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
        backgroundExecutor = HandlerExecutor(TAG)
    }

    private fun stopBackgroundThread() {
        backgroundExecutor?.shutdown()
        backgroundExecutor = null
    }

    private fun createCameraPreviewSession() {
        try {
            currentCameraDevice?.let { cameraDevice ->
                val texture = if(textureView != null) textureView!!.surfaceTexture else blankSurfaceTexture
                previewSize?.let { texture.setDefaultBufferSize(it.width, it.height) }
                val surface = Surface(texture)
                previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                previewRequestBuilder?.let { requestBuilder ->
                    val output = ArrayList<Surface>()
                    output.add(surface)
                    imageReader?.let { reader -> output.add(reader.surface) }
                    extractionReader?.let { reader ->
                        requestBuilder.addTarget(reader.surface)
                        output.add(reader.surface) }
                    requestBuilder.addTarget(surface)
                    cameraDevice.createCaptureSession( output,
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
            }

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        previewSize?.let {
            val activity = getActivity()
            val rotation = activity?.windowManager?.defaultDisplay?.rotation
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
        try {
            currentCameraDevice?.let { cameraDevice->
                val captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                imageReader?.let { reader-> captureBuilder.addTarget(reader.surface) }
                captureBuilder.set( CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                setAutoFlash(captureBuilder)
                val activity = getActivity()
                activity?.let { ac->
                    //val rotate = getOrientation( ac.windowManager.defaultDisplay.rotation )
                    //captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotate)
                    val sessionCaptureCallback = object : CameraCaptureSession.CaptureCallback() {
                        override fun onCaptureCompleted( session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                            Handler(Looper.getMainLooper()).post( captureCompletedRunnable )
                            unlockFocus()
                        }
                    }
                    captureSession?.let{ session ->
                        session.stopRepeating()
                        session.abortCaptures()
                        session.capture(captureBuilder.build(), sessionCaptureCallback, null)
                    }
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
            onTextureViewUpdated( texture )
        }
    }


    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        private fun process(result: CaptureResult) {
            when (state) {
                State.Preview -> { onPreview() }
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
            onExtract( image )
            image.close()
        }
    }
}