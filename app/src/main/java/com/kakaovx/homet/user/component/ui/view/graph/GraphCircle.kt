package com.kakaovx.homet.user.component.ui.view.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.skeleton.view.AnimatedDrawView
import android.graphics.RectF
import com.kakaovx.homet.user.util.Log


class GraphCircle@kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :  AnimatedDrawView(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName
    var total:Double = 0.0
    var amount:List<Double> = ArrayList()
        set(value) {
            if(value.isEmpty()) return
            var sum= 0.0
            field = value.map {
                sum += it
                return@map sum
            }
            Log.d(TAG , "field $field ")
            startAnimation(GraphUtil.ANIMATION_DURATION)
        }
    private var isDrawRect = false
    private var prevAmount:Double = 0.0
    private var currentAmount:Double = 0.0
    private lateinit var paints:ArrayList<Paint>
    private var rect:RectF? = null

    fun initSet(totalAmount:Double, colors:Array<String>) {
        total = totalAmount
        paints = ArrayList()
        colors.forEach {
            val paint = Paint()
            paint.color = Color.parseColor( it )
            paints.add( paint )
        }
    }

    private fun getCurrentPaint():Paint?{
        val findAmount = total * currentAmount / 360.0
        Log.d(TAG , "findAmount $findAmount ")
        val findValue = amount.find { it >= findAmount }
        Log.d(TAG , "findValue $findValue ")
        findValue ?: return null
        val findIdx = amount.indexOf(findValue)
        Log.d(TAG , "findIdx $findIdx ")
        if( findIdx >= paints.size) return null
        return paints[ findIdx ]
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if( rect != null && !isDrawRect) {
            canvas?.drawRect(rect!!, paints[0])
            isDrawRect = true
        }
        getCurrentPaint()?.let {
            Log.d(TAG , "draw paint $prevAmount $currentAmount")
            canvas?.drawArc(rect, prevAmount.toFloat(), currentAmount.toFloat(), true, it)
            prevAmount = currentAmount
        }

    }

    override fun onStart() {
        currentAmount = 0.0
        prevAmount = 0.0
        isDrawRect = false
        rect = RectF()
        rect?.set(0.0f, 0.0f, this.width.toFloat(), this.height.toFloat())
    }

    override fun onCompute(f: Int) {
        currentAmount = GraphUtil.easeOutSine(currentTime.toDouble(), 0.0 , 360.0 , duration.toDouble())
    }

    override fun onCompleted(f: Int) {
        currentAmount =  360.0
    }

}