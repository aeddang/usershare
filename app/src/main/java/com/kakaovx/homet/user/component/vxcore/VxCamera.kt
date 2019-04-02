package com.kakaovx.homet.user.component.vxcore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.util.Size
import android.view.Surface
import androidx.core.content.ContextCompat
import com.kakaovx.homet.user.component.model.VxCoreLiveData
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.util.AppDeviceExecutor
import com.kakaovx.homet.user.util.Log
import org.tensorflow.demo.env.ImageUtils
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class VxCamera(val context: Context) {

    val TAG = javaClass.simpleName

    private var deviceIO: AppDeviceExecutor? = null
    private var videoFps: Int = 15
    private val preDefinedVideoSize = Size(AppFeature.APP_FEATURE_VIDEO_WIDTH, AppFeature.APP_FEATURE_VIDEO_HEIGHT)
    private var computeVideoSize: Size? = null
    var videoWidth: Int = 0
    var videoHeight: Int = 0
    var existView: Boolean = false
    var surfaceTexture: SurfaceTexture? = null
    var cameraId: String? = null

    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private lateinit var previewRequest: CaptureRequest

    private var cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var frontCameraId: String? = null
    private var backCameraId: String? = null
    private var externalCameraId: String? = null

    private var imageReader: ImageReader? = null
    private var dummySurfaceTexture: SurfaceTexture = SurfaceTexture(10)

    private val cameraOpenCloseLock = Semaphore(1)

    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {
        deviceIO?.execute {
//            Log.d(TAG, "onImageAvailable() Thread id = [${Thread.currentThread().id}]")
            val image: Image? = it.acquireNextImage()
            image?.let {
                val liveData = VxCoreLiveData()
                liveData.cmd = AppConst.LIVE_DATA_VX_CMD_CAMERA
                liveData.cameraCmd = AppConst.HOMET_CAMERA_CMD_ON_IMAGE_AVAILABLE
                liveData.data = convertYUV420ToARGB8888(it)
                VxCoreObserver.setData(liveData)
                it.close()
            }
        }
    }

    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(cameraDevice: CameraDevice) {
            Log.e(TAG, "onOpened()")
            cameraOpenCloseLock.release()
            this@VxCamera.cameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            Log.e(TAG, "onDisconnected()")
            cameraOpenCloseLock.release()
            this@VxCamera.cameraDevice?.close()
            this@VxCamera.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            Log.e(TAG, "onError()")
            onDisconnected(cameraDevice)
        }

    }

    fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (i in planes.indices) {
            val buffer = planes[i].buffer
            if (yuvBytes[i] == null) {
//                Log.d(TAG, "Initializing buffer $i at size ${buffer.capacity()}")
                yuvBytes[i] = ByteArray(buffer.capacity())
            }
            buffer.get(yuvBytes[i])
        }
    }

    private fun convertYUV420ToARGB8888(imgYUV420: Image): IntArray {
        val previewWidth = videoWidth
        val previewHeight = videoHeight
        val yuvBytes = arrayOfNulls<ByteArray>(3)
        val rgbData = IntArray(previewWidth * previewHeight)
        val planes = imgYUV420.planes
        val yRowStride = planes[0].rowStride
        val uvRowStride = planes[1].rowStride
        val uvPixelStride = planes[1].pixelStride

        fillBytes(planes, yuvBytes)
        ImageUtils.convertYUV420ToARGB8888(yuvBytes[0], yuvBytes[1], yuvBytes[2],
                                           previewWidth, previewHeight,
                                           yRowStride, uvRowStride, uvPixelStride,
                                           rgbData)
        return rgbData
    }

    private fun convertYUV420ToNV21(imgYUV420: Image): ByteArray {
        val rez: ByteArray

        val buffer0 = imgYUV420.planes[0].buffer
        val buffer2 = imgYUV420.planes[2].buffer
        val buffer0Size = buffer0.remaining()
        val buffer2Size = buffer2.remaining()
        rez = ByteArray(buffer0Size + buffer2Size)

        buffer0.get(rez, 0, buffer0Size)
        buffer2.get(rez, buffer0Size, buffer2Size)

        return rez
    }

    private fun yuvImageToByteArray(image: Image): ByteArray {

        assert(image.format == ImageFormat.YUV_420_888)

        val width = image.width
        val height = image.height
        val planes = image.planes
        val result = ByteArray(width * height * 3 /2)

        var stride = planes[0].rowStride
        if (stride == width) {
            planes[0].buffer.get(result, 0, width)
        } else {
            for (row in 0 until height) {
                planes[0].buffer.position(row * stride)
                planes[0].buffer.get(result, row * width, width)
            }
        }

        stride = planes[1].rowStride
        assert (stride == planes[2].rowStride)
        val rowBytesCb = ByteArray(stride)
        val rowBytesCr = ByteArray(stride)

        for (row in 0 until height / 2) {
            val rowOffset = width * height + width / 2 * row
            planes[1].buffer.position(row * stride)
            planes[1].buffer.get(rowBytesCb, 0, width/2)
            planes[2].buffer.position(row * stride)
            planes[2].buffer.get(rowBytesCr, 0, width/2)

            for (col in 0 until width / 2) {
                result[rowOffset + col * 2] = rowBytesCr[col]
                result[rowOffset + col * 2 + 1] = rowBytesCb[col]
            }
        }
        return result
    }

    private fun getCameraIds() {
        Log.d(TAG, "getCameraIds()")
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)

            // We don't use a front facing camera in this sample.
            val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
            cameraDirection?.let {
//                Log.d(TAG, "getCameraIds() direction id = [$it]")
                when (it) {
                    CameraCharacteristics.LENS_FACING_FRONT -> frontCameraId = cameraId
                    CameraCharacteristics.LENS_FACING_BACK -> backCameraId = cameraId
                    CameraCharacteristics.LENS_FACING_EXTERNAL -> externalCameraId = cameraId
                }
            }
        }

        if (Build.MODEL == "SHIELD Android TV" || Build.DEVICE == "darcy") {
            externalCameraId?.apply {
                cameraId = externalCameraId
            }
        } else {
            frontCameraId?.apply {
                cameraId = frontCameraId
            }
//            backCameraId?.apply {
//                cameraId = backCameraId
//            }
        }
    }

    private fun setupCamera() {
        Log.d(TAG, "setupCamera()")
        cameraId?.let {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            val configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            Log.d(TAG, "NV21 = " + ImageFormat.NV21)
            Log.d(TAG, "YUV_420_8888 = " + ImageFormat.YUV_420_888)
            Log.d(TAG, "outputFormats = " + Arrays.toString(configs.outputFormats))

            imageReader = ImageReader.newInstance(videoWidth, videoHeight,
                ImageFormat.YUV_420_888, /*maxImages*/ videoFps).apply {
                setOnImageAvailableListener(onImageAvailableListener, deviceIO?.backgroundHandler)
            }
        } ?: Log.e(TAG, "camera id is null")
    }

    private fun createCameraPreviewSession() {
        Log.d(TAG, "createCameraPreviewSession() start")
        try {
            // This is the output Surface we need to start preview.
            val surfaceTexture = if (existView) surfaceTexture else dummySurfaceTexture

            surfaceTexture?.let {
                // We configure the size of default buffer to be the size of camera preview we want.
                surfaceTexture.setDefaultBufferSize(videoWidth, videoHeight)

                Log.i(TAG, "Opening camera preview: $videoWidth x $videoHeight")

                imageReader?.apply {
                    val mySurface = Surface(it)
                    // We set up a CaptureRequest.Builder with the output Surface.
                    previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                        CameraDevice.TEMPLATE_PREVIEW
                    )
                    previewRequestBuilder.addTarget(mySurface)
                    previewRequestBuilder.addTarget(surface)

                    // Here, we create a CameraCaptureSession for camera preview.
                    cameraDevice?.createCaptureSession(Arrays.asList(mySurface, surface),
                        object : CameraCaptureSession.StateCallback() {

                            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                                Log.e(TAG, "onConfigured()")
                                // The camera is already closed
                                if (cameraDevice == null) return

                                // When the session is ready, we start displaying the preview.
                                captureSession = cameraCaptureSession
                                try {
//                                    // Auto focus should be continuous for camera preview.
//                                    previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                                    // Auto focus should be continuous for camera preview.
                                    previewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                                    )
                                    // Flash is automatically enabled when necessary.
                                    previewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AE_MODE,
                                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                                    )

                                    // Finally, we start displaying the camera preview.
                                    previewRequest = previewRequestBuilder.build()
                                    captureSession?.setRepeatingRequest(previewRequest, null, deviceIO?.backgroundHandler)
                                } catch (e: CameraAccessException) {
                                    Log.e(TAG, e.toString())
                                }

                            }

                            override fun onConfigureFailed(session: CameraCaptureSession) {
                                Log.e(TAG, "onConfigureFailed()")
                            }
                        },
                        null)
                } ?: Log.e(TAG, "ImageReader is null")
            } ?: Log.e(TAG, "SurfaceTexture is null")
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }
        Log.d(TAG, "createCameraPreviewSession() end")
    }

    private fun startPreview() {
        Log.d(TAG, "startPreview()")
        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Need Camera Permission")
            return
        }
        setupCamera()
        cameraId?.let {
            try {
                // Wait for camera to open - 2.5 seconds is sufficient
                if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                    throw RuntimeException("Time out waiting to lock camera opening.")
                }
                cameraManager.openCamera(it, stateCallback, deviceIO?.backgroundHandler)
            } catch (e: CameraAccessException) {
                Log.e(TAG, e.toString())
            } catch (e: InterruptedException) {
                throw RuntimeException("Interrupted while trying to lock camera opening.", e)
            }
        }
    }

    private fun stopPreview() {
        Log.d(TAG, "stopPreview()")
        try {
            cameraOpenCloseLock.acquire()
            cameraDevice?.close()
            cameraDevice = null
            imageReader?.close()
            imageReader = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    fun setInputVideoSize(width: Int, height: Int) {
        computeVideoSize = Size(width, height)
    }

    fun initVxCamera() {
        Log.d(TAG, "initVxCamera()")
        getCameraIds()
    }

    fun pauseCamera() {
        Log.d(TAG, "pauseCamera()")
        stopPreview()
        deviceIO?.shutdown()
    }

    fun resumeCamera() {
        Log.d(TAG, "resumeCamera()")
        deviceIO = AppDeviceExecutor()
        startPreview()
    }

    fun changeCameraView(isFront: Boolean = true) {
        Log.d(TAG, "changeCameraView()")
        cameraId = if (isFront) frontCameraId else backCameraId
        stopPreview()
        startPreview()
    }

    fun isFrontCamera(): Boolean = (cameraId == frontCameraId)

    fun getInputVideoSize(): Size = computeVideoSize ?: preDefinedVideoSize

    fun destroyCamera() {
        Log.d(TAG, "destroyCamera()")
        pauseCamera()
        surfaceTexture = null
    }
}