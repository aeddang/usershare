package com.kakaovx.homet.user.component.ui.view.player

import android.content.Context
import android.util.AttributeSet
import java.util.ArrayList


class MotionRecognizerPlayer: VXPlayer {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    fun loadFromApi(apiPath:String) {

    }

    fun compareExtractionPose(pose: ArrayList<Array<FloatArray>>) :Boolean{
        return false
    }

}