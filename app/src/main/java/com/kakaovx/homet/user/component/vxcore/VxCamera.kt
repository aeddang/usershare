package com.kakaovx.homet.user.component.vxcore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.view.Surface
import android.view.TextureView
import androidx.core.content.ContextCompat
import com.kakaovx.homet.user.util.AppDeviceExecutor
import com.kakaovx.homet.user.util.Log
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class VxCamera(val context: Context) {

    val TAG = javaClass.simpleName

    private var deviceIO: AppDeviceExecutor? = null
    private var videoWidth: Int = 304
    private var videoHeight: Int = 304
    private var videoFps: Int = 15
    var existView: Boolean = false

    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private lateinit var previewRequest: CaptureRequest

    private var cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var cameraId: String? = null
    private var frontCameraId: String? = null
    private var backCameraId: String? = null

    private var imageReader: ImageReader? = null
    private var surfaceTexture: SurfaceTexture? = null
    private var dummySurfaceTexture: SurfaceTexture = SurfaceTexture(10)
    private var deviceOrientation = 0

    private val cameraOpenCloseLock = Semaphore(1)

    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {
        deviceIO?.execute {
            val image = it.acquireNextImage()
            val bytes = convertYUV420ToNV21(image)
            image.close()
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

    inner class MySurfaceTextureListener: TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            Log.d(TAG, "onSurfaceTextureSizeChanged()")
            surfaceTexture = surface
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//            Log.d(TAG, "onSurfaceTextureUpdated()")
            surfaceTexture = surface
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            Log.d(TAG, "onSurfaceTextureDestroyed()")
            surfaceTexture = surface
            return true
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            Log.d(TAG, "onSurfaceTextureAvailable()")
            surfaceTexture = surface
            resumeCamera()
        }
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


    private fun setCameraDisplayOrientation(displayRotation: Int, characteristics: CameraCharacteristics) {
        val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
        var result: Int

        if (cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
            result = (sensorOrientation + displayRotation) % 360
            result = (360 - result) % 360 // compensate the mirror
        } else { // back-facing
            result = (sensorOrientation - displayRotation + 360) % 360
        }

        Log.w(TAG, "Camera preview sensor orientation: $sensorOrientation")
        Log.w(TAG, "Camera preview orientation: $result")
        deviceOrientation = result
    }

    private fun getCameraIds() {
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)

            // We don't use a front facing camera in this sample.
            val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
            cameraDirection?.let {
                when (it) {
                    CameraCharacteristics.LENS_FACING_FRONT -> frontCameraId = cameraId
                    CameraCharacteristics.LENS_FACING_BACK -> backCameraId = cameraId
                }
            }
        }
        frontCameraId?.apply {
            cameraId = frontCameraId
        }
    }

    private fun setupCamera() {
        cameraId?.let {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            val configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            setCameraDisplayOrientation(0, characteristics)

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
        try {
            // This is the output Surface we need to start preview.
            val mySurfaceTexture = if (existView) surfaceTexture else dummySurfaceTexture
            mySurfaceTexture?.let {
                val mySurface = Surface(mySurfaceTexture)
                // We set up a CaptureRequest.Builder with the output Surface.
                previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
                )
                previewRequestBuilder.addTarget(mySurface)
                imageReader?.apply {
                    previewRequestBuilder.addTarget(surface)

                    // Here, we create a CameraCaptureSession for camera preview.
                    cameraDevice?.createCaptureSession(
                        Arrays.asList(mySurface, surface),
                        object : CameraCaptureSession.StateCallback() {

                            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                                // The camera is already closed
                                if (cameraDevice == null) return

                                // When the session is ready, we start displaying the preview.
                                captureSession = cameraCaptureSession
                                try {
                                    // Auto focus should be continuous for camera preview.
                                    previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

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
                        }, null)
                }
            } ?: Log.e(TAG, "surface is null")
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }
    }

    private fun startPreview() {
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

    fun initVxCamera() {
        Log.d(TAG, "initVxCamera()")
        getCameraIds()
    }

    fun initCameraView(view: TextureView) {
        Log.d(TAG, "initCameraView()")
        view.surfaceTextureListener = MySurfaceTextureListener()
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
        cameraId = if (isFront) frontCameraId else backCameraId
        stopPreview()
        startPreview()
    }

    fun isFrontCamera(): Boolean = (cameraId == frontCameraId)
}