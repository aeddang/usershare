package com.kakaovx.homet.user.component.ui.view.graph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet

class GraphBar@kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :  VXGraph(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName
    var total:Double = 0.0
    var amount:Double = 0.0
        set(value) {
            start = field
            field = value
            startAnimation(GraphUtil.ANIMATION_DURATION)
        }
    private var maxAmount:Double = 0.0
    private var currentAmount:Double = 0.0
    private var center:Float = 0.0F
    private var start:Double = 0.0
    private var paint = Paint()
    private var isVertical:Boolean = true

    fun initSet(totalAmount:Double, color:String,  isVertical:Boolean = true) {
        this.isVertical = isVertical
        paint.color = Color.parseColor( color )
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.SQUARE
        total = totalAmount
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(isVertical ) canvas?.drawLine(center,maxAmount.toFloat(),center,currentAmount.toFloat(),paint)
        else canvas?.drawLine(0.0f,center,currentAmount.toFloat(),center,paint)
        delegate?.let {
            val data = ArrayList<Pair<Double, Point>>()
            val pos = Math.round(currentAmount).toInt()
            val point =  if(isVertical) Point( 0 , pos) else Point( pos , 0 )
            data.add(Pair(amount, point))
            it.drawGraph(this, data)
        }
    }

    override fun onStart() {
        var w = 0.0f
        if( isVertical ){
            maxAmount = this.height.toDouble()
            w = this.width.toFloat()
        }else{
            maxAmount = this.width.toDouble()
            w = this.height.toFloat()
        }
        paint.strokeWidth = w
        center = w/2.0f
    }

    override fun onCompute(f: Int) {
        val amt = GraphUtil.easeOutSine(currentTime.toDouble(), start, amount-start , duration.toDouble()) / total
        currentAmount = if( isVertical ) maxAmount - (this.height * amt) else  this.width * amt
    }

    override fun onCompleted(f: Int) {
        currentAmount =  if( isVertical ) maxAmount - ( this.height * amount/ total ) else  this.width * amount/ total
    }

}