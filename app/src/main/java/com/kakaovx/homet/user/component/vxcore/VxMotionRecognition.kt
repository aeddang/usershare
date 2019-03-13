package com.kakaovx.homet.user.component.vxcore

import android.app.Application
import android.content.Context
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.util.Log
import com.kakaovx.posemachine.MaceWrapper
import com.kakaovx.posemachine.PoseMachine

class VxMotionRecognition(val app: Application, val context: Context) {

    val TAG = javaClass.simpleName

    private var poseMachine: PoseMachine? = null

    fun initMotionRecognition() {
        Log.i(TAG, "initMotionRecognition()")
        if (poseMachine == null) {
            poseMachine = PoseMachine(app, MaceWrapper.MODEL_TYPE.PersonLab, MaceWrapper.RUNTIME_TYPE.GPU)
            poseMachine?.apply {
                Log.d(TAG, "get inputWidth = [$inputWidth]")
                Log.d(TAG, "get inputHeight = [$inputHeight]")
            }
            return
        }
        Log.e(TAG, "Pose Machine already init()")
    }

    fun destroy() {
        Log.i(TAG, "destroy()")
        poseMachine?.apply {
            this.destroy()
        }
        poseMachine = null
    }

    fun getInputWidth() = poseMachine?.inputWidth ?: AppFeature.APP_FEATURE_VIDEO_WIDTH

    fun getInputHeight() = poseMachine?.inputHeight ?: AppFeature.APP_FEATURE_VIDEO_HEIGHT
}