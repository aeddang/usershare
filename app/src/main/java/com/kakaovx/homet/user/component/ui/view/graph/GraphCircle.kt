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

    private var prevAmount:Double = 0.0
    private var currentAmount:Double = 0.0
    private lateinit var paints:ArrayList<Paint>


    fun initSet(totalAmount:Double, colors:Array<String>) {
        total = totalAmount
        paints = ArrayList()
        colors.forEach {
            val paint = Paint()
            paint.color = Color.parseColor( it )
            paint.style = Paint.Style.STROKE
            paint.strokeCap = Paint.Cap.SQUARE
            paint.strokeWidth = 1.0f
            paints.add( paint )
        }
    }

    private fun getCurrentPaint():Paint?{
        val findAmount = total * currentAmount / 360.0
        val findValue = amount.find { it >= findAmount }
        findValue ?: return null
        val findIdx = amount.indexOf(findValue)
        if( findIdx >= paints.size) return null
        return paints[ findIdx ]
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var start = 0.0
        /*
        amount.forEachIndexed(idx, value) {
            //var end = if ( it >  currentAmount ) currentAmount else it

        }*/
    }

    override fun onStart() {
        currentAmount = 0.0
        prevAmount = 0.0
        //rect = null
        //paint = null

    }

    override fun onCompute(f: Int) {
        currentAmount = GraphUtil.easeOutSine(currentTime.toDouble(), 0.0 , 360.0 , duration.toDouble())
    }

    override fun onCompleted(f: Int) {
        currentAmount =  360.0
    }

}