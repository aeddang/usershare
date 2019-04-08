package com.kakaovx.homet.user.ui.page.player

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Size
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.thread.HandlerExecutor
import com.kakaovx.homet.user.util.Log
import java.util.*

class PopupPlayerViewModel(val repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi
    val pe = repo.poseEstimator
    private var deviceIO: HandlerExecutor = HandlerExecutor(TAG)
    private var isProcessingImage: Boolean = false
    val inputVideoSize:Size =  Size( pe.getInputWidth(), pe.getInputHeight())
    var pose:ArrayList<Array<FloatArray>>? = null

    var rgbFrameBitmap:Bitmap? = null
    var croppedBitmap:Bitmap? = null

    fun initPoseEstimator(){
        pe.initPoseEstimator()
    }

    fun poseDetect(data: IntArray, previewSize: Size, frameToCropTransform: Matrix ){
        if(isProcessingImage) return
        deviceIO.execute { poseEstimate(data, previewSize, frameToCropTransform) }
    }

    private fun poseEstimate(data: IntArray, previewSize: Size, frameToCropTransform: Matrix) {
        isProcessingImage = true

        rgbFrameBitmap = Bitmap.createBitmap(previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888)
        croppedBitmap = Bitmap.createBitmap(inputVideoSize.width, inputVideoSize.height, Bitmap.Config.ARGB_8888)
        rgbFrameBitmap ?: return
        croppedBitmap ?: return

        rgbFrameBitmap!!.setPixels(data, 0, previewSize.width, 0, 0, previewSize.width, previewSize.height)
        val canvas = Canvas(croppedBitmap!!)
        canvas.drawBitmap(rgbFrameBitmap!!, frameToCropTransform, null)
//        pose = pe.poseEstimate(croppedBitmap!!, PoseMachine.DataProcessCallback {
//            //Log.d(TAG, "onBitmapPrepared()")
//        })
        pose?.let { Log.d(TAG, "Detect Skeletons: [${it.size}]") }
        isProcessingImage = false
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        pose = null
        pe.destroy()
        VxCoreObserver.deleteObservers()
        deviceIO.shutdown()
        super.onCleared()
    }

}