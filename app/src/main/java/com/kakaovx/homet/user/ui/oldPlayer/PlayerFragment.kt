package com.kakaovx.homet.user.ui.oldPlayer

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Size
import android.util.TypedValue
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.view.BorderedText
import com.kakaovx.homet.user.component.ui.view.OverlayView
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.databinding.FragmentPlayerBinding
import com.kakaovx.homet.user.util.*
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import org.tensorflow.demo.env.ImageUtils
import java.util.*
import javax.inject.Inject

class PlayerFragment : DaggerFragment() {

    val TAG = javaClass.simpleName

    companion object {
        fun newInstance() = PlayerFragment()

        fun newInstance(id: String, url: String): PlayerFragment {
            val fragment = PlayerFragment()
            val bundle = Bundle()
            bundle.putString(AppConst.HOMET_VALUE_MOTION_ID, id)
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
    private var previewSize: Size? = null
    private lateinit var inputVideoSize: Size

    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null

    private var rgbFrameBitmap: Bitmap? = null
    private var croppedBitmap: Bitmap? = null
    private var borderedText: BorderedText? = null

    private var motionId: String? = null
    private var videoUrl: String? = null
    private var mediaPlayer: MediaPlayer? = null
    private var exoPlayer: ExoPlayer? = null

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

        previewSize?.let {
            val myActivity = activity ?: return
            val rotation = myActivity.windowManager.defaultDisplay.rotation
            val textureView = dataBinding.captureView ?: return
            val matrix = Matrix()
            val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
            val bufferRect = RectF(0f, 0f, it.height.toFloat(), it.width.toFloat())
            val centerX = viewRect.centerX()
            val centerY = viewRect.centerY()

            if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
                val scale = Math.max(
                    viewHeight.toFloat() / it.height,
                    viewWidth.toFloat() / it.width
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

            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
                inputVideoSize.width, inputVideoSize.height)

            previewSize?.let {
                // We fit the aspect ratio of TextureView to the size of preview we picked.
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textureView.setAspectRatio(it.width, it.height)
                } else {
                    textureView.setAspectRatio(it.height, it.width)
                }
                viewModel.setPreviewVideoSize(it)
                initMatrix(sensorOrientation, displayRotation)
            }
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
            sensorOrientation - AppUtil.getScreenOrientation(displayRotation)
        } else {
            sensorOrientation - AppUtil.getScreenOrientation(displayRotation)
        }

        Log.i(TAG, "Optimize orientation for MotionRecognition: [$calOrientation]")

        previewSize?.let {
            Log.i(TAG, "Initializing at size [${it.width}]x[${it.height}]")

            rgbFrameBitmap = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
            croppedBitmap = Bitmap.createBitmap(inputVideoSize.width, inputVideoSize.height, Bitmap.Config.ARGB_8888)
            frameToCropTransform = ImageUtils.getTransformationMatrix(
                it.width, it.height,
                inputVideoSize.width, inputVideoSize.height,
                calOrientation, true)

            if (viewModel.isFrontCamera()) {
                // front facing only
                frameToCropTransform?.postScale(1f, -1f, (inputVideoSize.width / 2).toFloat(), (inputVideoSize.height / 2).toFloat())
            }

            cropToFrameTransform = Matrix()
            frameToCropTransform?.invert(cropToFrameTransform)

            viewDisposables += viewModel.processImage(it,
                frameToCropTransform,
                rgbFrameBitmap,
                croppedBitmap)
        }
    }

    private fun drawInfo(canvas: Canvas, pose: ArrayList<Array<FloatArray>>) {
        previewSize?.let {
            val lines = viewModel.getDebugInfo(it.width, it.height)
            lines?.let {
                borderedText?.drawLines(canvas, 10.toFloat(), (canvas.height - 10).toFloat(), it)
            }
//        for (data in pose) {
//            for ((i, value) in data.withIndex()) {
//                Log.d(TAG, "pose position = [$i][${Arrays.toString(value)}]")
//            }
//        }
            viewModel.drawPose(canvas, pose)
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val userAgent = "AwesomePlayer"
        val extension = uri.lastPathSegment
        extension?.let {
            return if (it.contains("mp3") || it.contains("mp4")) ExtractorMediaSource.Factory(
                DefaultHttpDataSourceFactory(userAgent)
            ).createMediaSource(uri)
            else if (it.contains("m3u8")) HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
            else {
                val dashChunkSourceFactory = DefaultDashChunkSource.Factory(DefaultHttpDataSourceFactory("ua", DefaultBandwidthMeter()))
                val manifestDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
                DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri)
            }
        }
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
    }

    private fun load(videoPath:String) {
        val uri = Uri.parse(videoPath)
        val source: MediaSource = buildMediaSource(uri)
        exoPlayer?.prepare(source)
    }

    private fun initComponent() {
        dataBinding.captureView?.apply {
            viewModel.intCaptureView()
            surfaceTextureListener = object: TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture?, width: Int, height: Int) {
                    Log.d(TAG, "captureView onSurfaceTextureSizeChanged()")
                    configureTransform(width, height)
                }

                override fun onSurfaceTextureUpdated(texture: SurfaceTexture?) {
//                    Log.d(TAG, "captureView onSurfaceTextureUpdated()")
                }

                override fun onSurfaceTextureDestroyed(texture: SurfaceTexture?): Boolean {
                    Log.d(TAG, "captureView onSurfaceTextureDestroyed()")
                    viewModel.pauseCamera()
                    viewModel.setSurfaceTextureData(null)
                    return true
                }

                override fun onSurfaceTextureAvailable(texture: SurfaceTexture?, width: Int, height: Int) {
                    Log.d(TAG, "captureView onSurfaceTextureAvailable()")
                    viewModel.setSurfaceTextureData(texture)
                    viewModel.getCameraId()?.let {
                        setUpCameraOutputs(it, width, height)
                        configureTransform(width, height)
                        viewModel.resumeCamera()
                    } ?: Log.e(TAG, "camera ID is null")
                    exoPlayer?.run {
                        playWhenReady = true
                    }
                }
            }
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
            videoUrl?.let {
                exoPlayer = ExoPlayerFactory.newSimpleInstance(context)
                player = exoPlayer
                exoPlayer?.run {
                    addListener(object: Player.EventListener{
                        override fun onTimelineChanged( timeline: Timeline, manifest: Any? ,@Player.TimelineChangeReason reason: Int){}
                        override fun onTracksChanged( trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
                        override fun onLoadingChanged(isLoading: Boolean) {}
                        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {}
                        override fun onRepeatModeChanged(@Player.RepeatMode repeatMode: Int) {}
                        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
                        override fun onPlayerError(error: ExoPlaybackException) {}
                        override fun onPositionDiscontinuity(@Player.DiscontinuityReason reason: Int) {}
                        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
                        override fun onSeekProcessed() {}
                    })
                    load(it)
                }
            }
        }
    }

    private fun initSubscribe() {
        viewDisposables += viewModel.isLoading
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isLoading ->
                when (isLoading) {
                    true -> {
                        dataBinding.loadingLayout?.visibility = View.VISIBLE
                        dataBinding.playerLayout?.visibility = View.GONE

                    }
                    false -> {
                        dataBinding.loadingLayout?.visibility = View.GONE
                        dataBinding.playerLayout?.visibility = View.VISIBLE
                    }
                }
            }
        viewDisposables += viewModel.message
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value ->
                dataBinding.loadingCount?.text = value
            }
        viewDisposables += viewModel.startLoader()
        motionId?.let {
            viewDisposables += viewModel.getTrainerMotionData(it)
        } ?: Log.e(TAG, "motionId is null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        lifecycle += viewDisposables
        arguments?.apply {
            motionId = getString(AppConst.HOMET_VALUE_MOTION_ID)
            videoUrl = getString(AppConst.HOMET_VALUE_VIDEO_URL)
            Log.d(TAG, "onCreate() motion_id = [$motionId], video_url = [$videoUrl]")
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        exoPlayer?.apply {
            release()
        }
        exoPlayer = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreatedView()")
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false)

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated()")

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayerViewModel::class.java)

        initSubscribe()
        initComponent()

        viewModel.content.observe(this, Observer { workoutData ->
            workoutData?.run {
                free_motion_movie_url?.let {
                    videoUrl = it
                }
            }
        })

        viewModel.core.observe(this, Observer {
            if (it.cmd == AppConst.LIVE_DATA_VX_CMD_CAMERA) {
                when (it.cameraCmd) {
                    AppConst.HOMET_CAMERA_CMD_ON_IMAGE_AVAILABLE -> {
                        Log.d(TAG, "HOMET_CAMERA_CMD_ON_IMAGE_AVAILABLE")
                    }
                    AppConst.HOMET_CAMERA_CMD_REQUEST_DRAW -> {
                        dataBinding.overlayView?.apply {
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
