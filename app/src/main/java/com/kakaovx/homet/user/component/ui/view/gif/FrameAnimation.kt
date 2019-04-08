package com.kakaovx.homet.user.component.ui.view.gif

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.skeleton.view.AnimatedDrawView


class FrameAnimation@kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :  AnimatedDrawView(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName
    private var paint = Paint()
    private lateinit var bitmap:Bitmap
    private var low = 1
    private var column = 1
    private var totalFrame = 1
    private var frameWidth = 1
    private var frameHeight = 1

    private var src:Rect? = null
    private var dest:Rect? = null
    private var currentFrame:Int = -1
    private var dr:Int = 0
    var frame:Int = 0
        set(value) {
            field = value
            if( currentFrame == field ) return
            startAnimation(-1)
        }

    fun initSet(resId:Int, low:Int, column:Int, totalFrame:Int = -1) {
        this.low = low
        this. column = column
        this.totalFrame = if( totalFrame != -1) totalFrame else low * column
        bitmap = BitmapFactory.decodeResource(resources, resId)
        paint.style = Paint.Style.FILL
        frameWidth = bitmap.width / column
        frameHeight = bitmap.height / low
        this.frame = 0
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, src, dest, paint)
    }

    override fun onStart() {
        dest = Rect(0, 0, width, height)
    }

    override fun onCompute(f: Int) {
        currentFrame ++
        if(currentFrame == totalFrame) currentFrame = 0
        val idxX = currentFrame % this.column
        val idxY = Math.floor((currentFrame / this.column).toDouble())
        val tx = idxX * this.width
        val ty = idxY * this.height
        src = Rect(tx, ty.toInt(), frameWidth, frameHeight)
        if(currentFrame == frame) onCompute(f)

    }

    override fun onCompleted(f: Int) {

    }
}