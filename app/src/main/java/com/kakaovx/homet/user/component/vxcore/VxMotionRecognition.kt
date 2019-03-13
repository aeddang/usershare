package com.kakaovx.homet.user.component.vxcore

import android.app.Application
import android.content.Context
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
                Log.d(TAG, "get inputWidth = [$inputHeight]")
                Log.d(TAG, "get inputHeight = [$inputHeight]")
            }
            return
        }
        Log.e(TAG, "Pose Machine already init()")
    }

    fun destroy() {
        Log.i(TAG, "destroy()")
        poseMachine?.apply {
            destroy()
        }
        poseMachine = null
    }
}