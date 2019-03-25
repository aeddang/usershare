package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.SurfaceView

class OverlaySurfaceView: SurfaceView{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    private var delegate: Delegate? = null
    interface Delegate{
        fun drawCanvas(canvas: Canvas){}
    }
    fun setOnDrawListener( _delegate: Delegate? ){ delegate = _delegate }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {  delegate?.drawCanvas(it) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.setZOrderOnTop(true)
        setBackgroundColor(Color.TRANSPARENT)
        holder.setFormat(PixelFormat.TRANSLUCENT)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        delegate = null
    }

}