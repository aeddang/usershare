package com.kakaovx.homet.user.ui.page.player

import android.graphics.Matrix
import android.graphics.Typeface
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.user.R
import com.kakaovx.homet.lib.page.PageGestureView
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageDividedGestureFragment
import com.kakaovx.homet.user.component.ui.view.BorderedText
import com.kakaovx.homet.user.component.ui.view.camera.draw
import com.kakaovx.homet.user.component.ui.view.camera.motionExtract
import com.kakaovx.homet.user.constant.AppFeature
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.popup_player.*
import org.tensorflow.demo.env.ImageUtils

import javax.inject.Inject

class PopupPlayer : RxPageDividedGestureFragment() {

    private val TAG = javaClass.simpleName
    override fun getLayoutResId(): Int { return R.layout.popup_player }
    override fun getGestureView(): PageGestureView { return gestureView }
    override fun getContentsView(): View { return contents }
    override fun getBackgroundView(): View { return bg }
    override fun getDividedView(): View { return divided }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: PopupPlayerViewModel
    private var borderedText: BorderedText? = null

    private var cIdx = 0
    private var captureCompletedRunnable: Runnable = Runnable { onCapture() }

    override fun onCreatedView() {
        super.onCreatedView()
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PopupPlayerViewModel::class.java)

        val textSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, AppFeature.APP_FEATURE_TEXT_SIZE_DIP, resources.displayMetrics
        )
        borderedText = BorderedText(textSizePx)
        borderedText?.setTypeface(Typeface.MONOSPACE)

        camera.customSize = viewModel.inputVideoSize
        viewModel.initMotionRecognition()
        Log.i(TAG, "viewModel.inputVideoSize [${ viewModel.inputVideoSize.width}]x[${ viewModel.inputVideoSize.height}]")
        player.initPlayer()
        player.load("http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4")
    }

    override fun onDestroyedView() {
        super.onDestroyedView()
        borderedText = null
    }


    override fun onSubscribe() {

        camera.motionExtract().subscribe { pose->
            camera.cameraOutputSize?.let { outputSize->
                Log.i(TAG, "Initializing at size [${outputSize.width}]x[${outputSize.height}]")
                val inputSize =  viewModel.inputVideoSize
                var rotate = camera.getOrientation()
                val frameToCropTransform = ImageUtils.getTransformationMatrix(
                    outputSize.width, outputSize.height,
                    inputSize.width, inputSize.height,
                    rotate, true)

                if ( camera.isSwap()  ) {
                    frameToCropTransform.postScale(-1f, 1f, (inputSize.width / 2).toFloat(), (inputSize.height / 2).toFloat())
                }

                val cropToFrameTransform = Matrix()
                frameToCropTransform?.invert(cropToFrameTransform)
                viewModel.poseDetect(pose, outputSize, frameToCropTransform)

            }
        }.apply { disposables.add(this) }

        camera.draw().subscribe{ canvas ->
            camera.previewSize?.let { prev ->
                viewModel.pose?.let{ viewModel.mr.drawPose(canvas, it) }
                val lines = viewModel.mr.getDebugInfo(prev.width, prev.height)
                lines?.let { borderedText?.drawLines(canvas, 10.toFloat(), (canvas.height - 10).toFloat(), it) }
            }
            viewModel.rgbFrameBitmap?.let {
                cIdx ++
                if( cIdx % 30 == 1){
                    android.os.Handler(Looper.getMainLooper()).post( captureCompletedRunnable )
                }

            }
        }.apply { disposables.add(this) }
    }

    private fun onCapture(){
        imageView.setImageBitmap(viewModel.croppedBitmap)
    }


    override fun onResume() {
        super.onResume()
        player.onResume()
        camera.onResume()
    }

    override fun onPause() {
        super.onPause()
        player.onPause()
        camera.onPause()
    }

}