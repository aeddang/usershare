package com.kakaovx.homet.user.ui.page.etc.player

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Size
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.VxCoreObserver
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.util.AppDeviceExecutor
import com.kakaovx.homet.user.util.Log
import com.kakaovx.posemachine.PoseMachine
import java.util.*

class PopupPlayerViewModel(val repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi
    val mr = repo.mr
    private var deviceIO: AppDeviceExecutor = AppDeviceExecutor()
    private var isProcessingImage: Boolean = false
    val inputVideoSize:Size =  Size( mr.getInputWidth(), mr.getInputHeight())
    var pose:ArrayList<Array<FloatArray>>? = null

    var rgbFrameBitmap:Bitmap? = null
    var croppedBitmap:Bitmap? = null

    fun initMotionRecognition(){
        mr.initMotionRecognition()
    }

    fun poseDetect(data: IntArray, previewSize: Size, frameToCropTransform: Matrix ){
        if(isProcessingImage) return
        deviceIO.execute { poseEstimate(data, previewSize, frameToCropTransform) }
    }

    fun poseEstimate(data: IntArray, previewSize: Size, frameToCropTransform: Matrix) {
        isProcessingImage = true

        rgbFrameBitmap = Bitmap.createBitmap(previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888)
        croppedBitmap = Bitmap.createBitmap(inputVideoSize.width, inputVideoSize.height, Bitmap.Config.ARGB_8888)
        rgbFrameBitmap ?: return
        croppedBitmap ?: return

        rgbFrameBitmap!!.setPixels(data, 0, previewSize.width, 0, 0, previewSize.width, previewSize.height)
        val canvas = Canvas(croppedBitmap!!)
        canvas.drawBitmap(rgbFrameBitmap!!, frameToCropTransform, null)
        pose = mr.poseEstimate(croppedBitmap!!, PoseMachine.DataProcessCallback {
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