package com.kakaovx.homet.user.component.ui.view.graph

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.skeleton.view.AnimatedDrawView
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
                val rt = it / total * 360.0
                sum += rt
                return@map sum
            }
            Log.d(TAG , "field $field ")
            startAnimation(GraphUtil.ANIMATION_DURATION_LONG)
        }

    private var prevAmount:Double = 0.0
    private var currentAmount:Double = 0.0
    private var startAngle:Float = -90.0f
    private var centerX = 0.0f
    private var centerY = 0.0f
    private lateinit var paints:ArrayList<Paint>

    private lateinit var rectF:RectF
    private lateinit var camera:Camera
    private var isApplyCamera:Boolean = false

    fun initSet(totalAmount:Double, colors:Array<String>) {
        total = totalAmount
        paints = ArrayList()
        colors.forEach {
            val paint = Paint()
            paint.color = Color.parseColor( it )
            paints.add( paint )
        }
        rectF = RectF()
        camera = Camera()
        camera.rotateY(-40.0f)
        camera.rotateX(20.0f)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var start = 0.0
        camera.save()
        canvas?.save()
        canvas?.translate(centerX, centerY)
        camera.applyToCanvas(canvas)
        canvas?.translate(-centerX, -centerY)
        canvas?.rotate(startAngle, centerX, centerY)
        amount.forEachIndexed { idx, value ->
            val end = if ( value >  currentAmount ) currentAmount else value
            if( end <= start) return
            if(idx >= paints.size ) return
            val paint = paints[ idx ]
            val s = start.toFloat()
            val e = end.toFloat() - s
            canvas?.drawArc(rectF,s, e, true, paint)
            start = end
        }
        canvas?.restore()
        camera.restore()
    }

    override fun onStart() {
        val marginX = width.toFloat() * 0.1f
        val marginY = height.toFloat() * 0.1f
        val w = width.toFloat() - marginX
        val h = height.toFloat() - marginY
        centerX = width.toFloat()/2.0f
        centerY = height.toFloat()/2.0f
        rectF.set(marginX, marginY, w, h)
        currentAmount = 0.0
        prevAmount = 0.0
    }

    override fun onCompute(f: Int) {
        currentAmount = GraphUtil.easeOutSine(currentTime.toDouble(), 0.0 , 360.0 , duration.toDouble())
    }

    override fun onCompleted(f: Int) {
        currentAmount =  360.0
    }

}