package com.kakaovx.homet.user.component.ui.skeleton.view.camera

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.view.View

class AutoFitTextureView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    TextureView(context, attrs, defStyle) {
    private val TAG = javaClass.simpleName
    private var ratioWidth = 0
    private var ratioHeight = 0

    fun setAspectRatio(width: Int, height: Int) {
        if (width < 0 || height < 0) {
            throw IllegalArgumentException("Size cannot be negative.")
        }

        ratioWidth = width
        ratioHeight = height
        requestLayout()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)

        if (0 == ratioWidth || 0 == ratioHeight) {
            setMeasuredDimension(width, height)
            this.translationX = 0F
            this.translationY = 0F
        } else {
            if (width < height * ratioWidth / ratioHeight) {
                val h = width * ratioHeight / ratioWidth
                setMeasuredDimension(width, h )
                translationX = 0F
                translationY = ((height - h)/2).toFloat()
            } else {
                val w = height * ratioWidth / ratioHeight
                setMeasuredDimension(w, height)
                translationX = ((width - w)/2).toFloat()
                translationY = 0F
            }

        }
    }

}
