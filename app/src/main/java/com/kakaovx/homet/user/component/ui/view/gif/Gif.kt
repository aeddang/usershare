package com.kakaovx.homet.user.component.ui.view.gif

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.skeleton.view.AnimatedDrawView
import com.kakaovx.homet.user.component.ui.view.graph.GraphUtil

class Gif@kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :  AnimatedDrawView(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName

    fun initSet(totalAmount:Double, color:String,  isVertical:Boolean = true) {

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    override fun onStart() {

    }

    override fun onCompute(f: Int) {

    }

    override fun onCompleted(f: Int) {

    }
}