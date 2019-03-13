package com.kakaovx.homet.user.component.vxcore

import android.app.Application
import android.content.Context
import com.kakaovx.posemachine.MaceWrapper
import com.kakaovx.posemachine.PoseMachine

class VxMotionRecognition(val app: Application, val context: Context) {

    private var poseMachine: PoseMachine? = null


    init {
        poseMachine = PoseMachine(app, MaceWrapper.MODEL_TYPE.PersonLab, MaceWrapper.RUNTIME_TYPE.GPU)
    }
}