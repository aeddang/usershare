package com.kakaovx.homet.user.ui.player

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.text.TextUtils
import android.util.Size
import android.util.TypedValue
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.view.BorderedText
import com.kakaovx.homet.user.component.ui.view.OverlayView
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.databinding.FragmentPlayerBinding
import com.kakaovx.homet.user.util.*
import dagger.android.support.DaggerFragment
import org.tensorflow.demo.env.ImageUtils
import java.util.*
import javax.inject.Inject

class PlayerFragment : DaggerFragment() {

    val TAG = javaClass.simpleName

    companion object {
        fun newInstance() = PlayerFragment()

        fun newInstance(url: String): PlayerFragment {
            val fragment = PlayerFragment()
            val bundle = Bundle()
            bundle.putString(AppConst.HOMET_VALUE_VIDEO_URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewDisposables = AppFragmentAutoClearedDisposable(this)

    @Inject
    lateinit var viewModelFactory: PlayerViewModelFactory
    private lateinit var viewModel: PlayerViewModel
    private lateinit var dataBinding: FragmentPlayerBinding

    /**
     * The camera preview size will be chosen to be the smallest frame by pixel size capable of
     * containing a DESIRED_SIZE x DESIRED_SIZE square.
     */
    private val MINIMUM_PREVIEW_SIZE = 320

    /**
     * The [android.util.Size] of camera preview.
     */
    private lateinit var previewSize: Size
    private lateinit var inputVideoSize: Size

    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null

    private var rgbFrameBitmap: Bitmap? = null
    private var croppedBitmap: Bitmap? = null
    private var borderedText: BorderedText? = null

    private var videoUrl: String? = null
    private var mediaPlayer: MediaPlayer? = null

    private val cameraListener = object: TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture?, width: Int, height: Int) {
            Log.d(TAG, "onSurfaceTextureSizeChanged()")
            configureTransform(width, height)
        }

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture?) {
//            Log.d(TAG, "onSurfaceTextureUpdated()")
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture?): Boolean {
            Log.d(TAG, "onSurfaceTextureDestroyed()")
            viewModel.pauseCamera()
            viewModel.setSurfaceTextureData(null)
            return true
        }

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture?, width: Int, height: Int) {
            Log.d(TAG, "onSurfaceTextureAvailable()")
            viewModel.setSurfaceTextureData(texture)
            viewModel.getCameraId()?.let {
                setUpCameraOutputs(it, width, height)
                configureTransform(width, height)
                viewModel.resumeCamera()
            }
        }
    }

//    /**
//     * Max preview width that is guaranteed by Camera2 API
//     */
//    private val MAX_PREVIEW_WIDTH = 1920

//    /**
//     * Max preview height that is guaranteed by Camera2 API
//     */
//    private val MAX_PREVIEW_HEIGHT = 1080

//    /**
//     * Given `choices` of `Size`s supported by a camera, choose the smallest one that
//     * is at least as large as the respective texture view size, and that is at most as large as
//     * the respective max size, and whose aspect ratio matches with the specified value. If such
//     * size doesn't exist, choose the largest one that is at most as large as the respective max
//     * size, and whose aspect ratio matches with the specified value.
//     *
//     * @param choices           The list of sizes that the camera supports for the intended
//     *                          output class
//     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
//     * @param textureViewHeight The height of the texture view relative to sensor coordinate
//     * @param maxWidth          The maximum width that can be chosen
//     * @param maxHeight         The maximum height that can be chosen
//     * @param aspectRatio       The aspect ratio
//     * @return The optimal `Size`, or an arbitrary one if none were big enough
//     */
//    private fun chooseOptimalSize(
//        choices: Array<Size>,
//        textureViewWidth: Int,
//        textureViewHeight: Int,
//        maxWidth: Int,
//        maxHeight: Int,
//        aspectRatio: Size
//    ): Size {
//        Log.d(TAG, "chooseOptimalSize()")
//
//        // Collect the supported resolutions that are at least as big as the preview Surface
//        val bigEnough = ArrayList<Size>()
//        // Collect the supported resolutions that are smaller than the preview Surface
//        val notBigEnough = ArrayList<Size>()
//        val w = aspectRatio.width
//        val h = aspectRatio.height
//        for (option in choices) {
//            if (option.width <= maxWidth && option.height <= maxHeight &&
//                option.height == option.width * h / w) {
//                if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
//                    bigEnough.add(option)
//                } else {
//                    notBigEnough.add(option)
//                }
//            }
//        }
//
//        // Pick the smallest of those big enough. If there is no one big enough, pick the
//        // largest of those not big enough.
//        if (bigEnough.size > 0) {
//            return Collections.min(bigEnough, CompareSizesByArea())
//        } else if (notBigEnough.size > 0) {
//            return Collections.max(notBigEnough, CompareSizesByArea())
//        } else {
//            android.util.Log.e(TAG, "Couldn't find any suitable preview size")
//            return choices[0]
//        }
//    }

//    /**
//     * Sets up member variables related to camera.
//     *
//     * @param width  The width of available size for camera preview
//     * @param height The height of available size for camera preview
//     */
//    private fun setUpCameraOutputs(cameraId: String, width: Int, height: Int) {
//        Log.d(TAG, "setUpCameraOutputs()")
//
//        val myActivity = activity ?: return
//        val textureView = dataBinding.captureView ?: return
//        val manager = myActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
//
//        try {
//            val characteristics = manager.getCameraCharacteristics(cameraId)
//            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: return
//
//            // Find out if we need to swap dimension to get the preview size relative to sensor
//            // coordinate.
//            val displayRotation = myActivity.windowManager.defaultDisplay.rotation
//            val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: return
//            val swappedDimensions = areDimensionsSwapped(sensorOrientation, displayRotation)
//
//            val displaySize = Point()
//            myActivity.windowManager.defaultDisplay.getSize(displaySize)
//            val rotatedPreviewWidth = if (swappedDimensions) height else width
//            val rotatedPreviewHeight = if (swappedDimensions) width else height
//            var maxPreviewWidth = if (swappedDimensions) displaySize.y else displaySize.x
//            var maxPreviewHeight = if (swappedDimensions) displaySize.x else displaySize.y
//
//            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) maxPreviewWidth = MAX_PREVIEW_WIDTH
//            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) maxPreviewHeight = MAX_PREVIEW_HEIGHT
//
//            // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
//            // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
//            // garbage capture data.
//
//            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
//                rotatedPreviewWidth, rotatedPreviewHeight,
//                maxPreviewWidth, maxPreviewHeight,
//                inputVideoSize)
//
//            // We fit the aspect ratio of TextureView to the size of preview we picked.
//            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                textureView.setAspectRatio(previewSize.width, previewSize.height)
//            } else {
//                textureView.setAspectRatio(previewSize.height, previewSize.width)
//            }
//            viewModel.setPreviewVideoSize(previewSize)
//            initMatrix(sensorOrientation)
//        } catch (e: CameraAccessException) {
//            Log.e(TAG, e.toString())
//        } catch (e: NullPointerException) {
//            // Currently an NPE is thrown when the Camera2API is used but not supported on the
//            // device this code runs.
//            Log.e(TAG, e.toString())
//        }
//    }

//    /**
//     * Determines if the dimensions are swapped given the phone's current rotation.
//     *
//     * @param displayRotation The current rotation of the display
//     *
//     * @return true if the dimensions are swapped, false otherwise.
//     */
//    private fun areDimensionsSwapped(sensorOrientation: Int, displayRotation: Int): Boolean {
//        Log.d(TAG, "areDimensionsSwapped()")
//
//        var swappedDimensions = false
//        when (displayRotation) {
//            Surface.ROTATION_0, Surface.ROTATION_180 -> {
//                if (sensorOrientation == 90 || sensorOrientation == 270) {
//                    swappedDimensions = true
//                }
//            }
//            Surface.ROTATION_90, Surface.ROTATION_270 -> {
//                if (sensorOrientation == 0 || sensorOrientation == 180) {
//                    swappedDimensions = true
//                }
//            }
//            else -> {
//                Log.e(TAG, "Display rotation is invalid: $displayRotation")
//            }
//        }
//        return swappedDimensions
//    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the minimum of both, or an exact match if possible.
     *
     * @param choices The list of sizes that the camera supports for the intended output class
     * @param width The minimum desired width
     * @param height The minimum desired height
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private fun chooseOptimalSize(choices: Array<Size>, width: Int, height: Int): Size {
        val minSize = Math.max(Math.min(width, height), MINIMUM_PREVIEW_SIZE)
        val desiredSize = Size(width, height)

        // Collect the supported resolutions that are at least as big as the preview Surface
        var exactSizeFound = false
        val bigEnough = ArrayList<Size>()
        val tooSmall = ArrayList<Size>()
        for (option in choices) {
            if (option == desiredSize) {
                // Set the size but don't return yet so that remaining sizes will still be logged.
                exactSizeFound = true
            }

            if (option.height >= minSize && option.width >= minSize) {
                bigEnough.add(option)
            } else {
                tooSmall.add(option)
            }
        }

        Log.i(TAG, "Desired size: [$desiredSize], min size: $minSize x $minSize")
        val bigValue = TextUtils.join(", ", bigEnough)
        Log.i(TAG, "Valid preview sizes: [$bigValue]")
        val smallValue = TextUtils.join(", ", tooSmall)
        Log.i(TAG, "Rejected preview sizes: [$smallValue]")

        if (exactSizeFound) {
            Log.i(TAG, "Exact size match found.")
            return desiredSize
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size > 0) {
            val chosenSize = Collections.min(bigEnough, CompareSizesByArea())
            Log.i(TAG, "Chosen size: ${chosenSize.width}x${chosenSize.height}")
            return chosenSize
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size")
            return choices[0]
        }
    }

    /**
     * Configures the necessary [android.graphics.Matrix] transformation to `textureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `textureView` is fixed.
     *
     * @param viewWidth  The width of `textureView`
     * @param viewHeight The height of `textureView`
     */
    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        Log.d(TAG, "configureTransform()")

        val myActivity = activity ?: return
        val rotation = myActivity.windowManager.defaultDisplay.rotation
        val textureView = dataBinding.captureView ?: return
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()

        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            val scale = Math.max(
                viewHeight.toFloat() / previewSize.height,
                viewWidth.toFloat() / previewSize.width
            )
            with(matrix) {
                setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
                postScale(scale, scale, centerX, centerY)
                postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
            }
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        textureView.setTransform(matrix)
    }


    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private fun setUpCameraOutputs(cameraId: String, width: Int, height: Int) {
        Log.d(TAG, "setUpCameraOutputs()")

        val myActivity = activity ?: return
        val textureView = dataBinding.captureView ?: return
        val manager = myActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: return

            // Find out if we need to swap dimension to get the preview size relative to sensor
            // coordinate.
            val displayRotation = myActivity.windowManager.defaultDisplay.rotation
            val sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: return

            // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
            // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
            // garbage capture data.

//            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
//                                            width, height)
            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
                inputVideoSize.width, inputVideoSize.height)

            // We fit the aspect ratio of TextureView to the size of preview we picked.
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(previewSize.width, previewSize.height)
            } else {
                textureView.setAspectRatio(previewSize.height, previewSize.width)
            }
            viewModel.setPreviewVideoSize(previewSize)
            initMatrix(sensorOrientation, displayRotation)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: NullPointerException) {
            Log.e(TAG, e.toString())
        }
    }

    private fun initMatrix(sensorOrientation: Int, displayRotation: Int) {
        val textSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, AppFeature.APP_FEATURE_TEXT_SIZE_DIP, resources.displayMetrics
        )
        borderedText = BorderedText(textSizePx)
        borderedText?.setTypeface(Typeface.MONOSPACE)

        Log.i(TAG, "Camera is front? : [${viewModel.isFrontCamera()}]")
        Log.i(TAG, "Camera sensor orientation : [$sensorOrientation]")
        Log.i(TAG, "window orientation : [${AppUtil.getScreenOrientation(displayRotation)}]")

        val calOrientation = if (viewModel.isFrontCamera()) {
            sensorOrientation - AppUtil.getScreenOrientation(displayRotation) - 180
        } else {
            sensorOrientation - AppUtil.getScreenOrientation(displayRotation)
        }

        Log.i(TAG, "Optimize orientation for MotionRecognition: [$calOrientation]")

        Log.i(TAG, "Initializing at size [${previewSize.width}]x[${previewSize.height}]")

        rgbFrameBitmap = Bitmap.createBitmap(previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888)
        croppedBitmap = Bitmap.createBitmap(inputVideoSize.width, inputVideoSize.height, Bitmap.Config.ARGB_8888)
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewSize.width, previewSize.height,
            inputVideoSize.width, inputVideoSize.height,
            calOrientation, true)

        cropToFrameTransform = Matrix()
        frameToCropTransform?.invert(cropToFrameTransform)

        viewDisposables += viewModel.processImage(previewSize,
                                                  frameToCropTransform,
                                                  rgbFrameBitmap,
                                                  croppedBitmap)
    }

    private fun drawInfo(canvas: Canvas, pose: ArrayList<Array<FloatArray>>) {
//        Log.d(TAG, "drawInfo() Thread id = [${Thread.currentThread().id}]")
        val lines = viewModel.getDebugInfo(previewSize.width, previewSize.height)
        lines?.let {
//            Log.d(TAG, "drawInfo() [$lines]")
            borderedText?.drawLines(canvas, 10.toFloat(), (canvas.height - 10).toFloat(), it)
        }
        for (data in pose) {
            viewModel.drawPose(canvas, pose)
        }
    }

    private fun initComponent() {
        dataBinding.captureView?.apply {
            viewModel.intCaptureView()
            surfaceTextureListener = cameraListener
            viewModel.setExistView(true)
            inputVideoSize = viewModel.getInputVideoSize()
        }
        dataBinding.overlayView?.apply {
            Log.d(TAG, "initComponent() Thread id = [${Thread.currentThread().id}]")
            addCallback(object: OverlayView.DrawCallback {
                override fun drawCallback(canvas: Canvas, pose: ArrayList<Array<FloatArray>>) {
                    this@PlayerFragment.drawInfo(canvas, pose)
                }
            })
        }
        dataBinding.rendererView?.apply {
            surfaceTextureListener = object: TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture?, width: Int, height: Int) {
                    Log.d(TAG, "onSurfaceTextureSizeChanged()")
                }

                override fun onSurfaceTextureUpdated(texture: SurfaceTexture?) {
//            Log.d(TAG, "onSurfaceTextureUpdated()")
                }

                override fun onSurfaceTextureDestroyed(texture: SurfaceTexture?): Boolean {
                    Log.d(TAG, "onSurfaceTextureDestroyed()")
                    return true
                }

                override fun onSurfaceTextureAvailable(texture: SurfaceTexture?, width: Int, height: Int) {
                    Log.d(TAG, "onSurfaceTextureAvailable()")
                    val surface = Surface(texture)
                    videoUrl?.let {
                        mediaPlayer = MediaPlayer()
                        val audioAttributes = AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                            .build()
                        mediaPlayer?.run {
                            setAudioAttributes(audioAttributes)
                            setDataSource(it)
                            setSurface(surface)
                            prepare()
                            start()
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        lifecycle += viewDisposables
        arguments?.apply {
            videoUrl = getString(AppConst.HOMET_VALUE_VIDEO_URL)
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView()")
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated()")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerViewModel::class.java)

        initComponent()

        viewModel.core.observe(this, Observer {
            if (it.cmd == AppConst.LIVE_DATA_CMD_CAMERA) {
                when (it.cameraCmd) {
                    AppConst.HOMET_CAMERA_CMD_REQUEST_DRAW -> {
                        dataBinding.overlayView?.apply {
//                            Log.d(TAG, "requestDraw() overlayView postInvalidate")
                            it.poseData?.let { poseData ->
                                pose.clear()
                                pose.addAll(poseData)
                            }
                            postInvalidate()
                        }
                    }
                    else -> {
                        Log.e(TAG, "wrong camera cmd")
                    }
                }
            } else {
                Log.e(TAG, "wrong cmd")
            }
        })
    }
}
