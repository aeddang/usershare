package com.kakaovx.homet.user.ui.viewModel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.VxCoreLiveData
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.component.network.model.WorkoutData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.AppDeviceExecutor
import com.kakaovx.homet.user.util.Log
import com.kakaovx.posemachine.PoseMachine
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit

class PopupPlayerViewModel(val repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi
    val mr = repo.mr
    private var deviceIO: AppDeviceExecutor = AppDeviceExecutor()
    private var isProcessingImage: Boolean = false
    val inputVideoSize:Size =  Size( mr.getInputWidth(), mr.getInputHeight())
    var pose:ArrayList<Array<FloatArray>>? = null
    fun initMotionRecognition(){
        mr.initMotionRecognition()
    }

    fun poseDetect(data: IntArray, previewSize: Size, frameToCropTransform: Matrix ){
        if(isProcessingImage) return
        deviceIO.execute { poseEstimate(data, previewSize, frameToCropTransform) }
    }

    fun poseEstimate(data: IntArray, previewSize: Size, frameToCropTransform: Matrix) {
        isProcessingImage = true

        var rgbFrameBitmap = Bitmap.createBitmap(previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888)
        var croppedBitmap = Bitmap.createBitmap(inputVideoSize.width, inputVideoSize.height, Bitmap.Config.ARGB_8888)
        rgbFrameBitmap ?: return
        croppedBitmap ?: return

        rgbFrameBitmap.setPixels(data, 0, previewSize.width, 0, 0, previewSize.width, previewSize.height)
        val canvas = Canvas(croppedBitmap)
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null)
        pose = mr.poseEstimate(croppedBitmap, PoseMachine.DataProcessCallback {
            //Log.d(TAG, "onBitmapPrepared()")
        })
        pose?.let { Log.d(TAG, "Detect Skeletons: [${it.size}]") }
        isProcessingImage = false
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        pose = null
        mr.destroy()
        VxCoreObserver.deleteObservers()
        deviceIO.shutdown()
        super.onCleared()
    }

}