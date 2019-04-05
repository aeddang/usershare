package com.kakaovx.homet.user.component.ui.view.graph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.kakaovx.homet.user.util.Log


class GraphCircle@kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :  VXGraph(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName
    var total:Double = 0.0
    var amount:List<Double> = ArrayList()
        set(value) {
            if(value.isEmpty()) return
            field = value.map {
                val rt = it / total * totalDegree
                return@map rt
            }
            Log.d(TAG , "field $field ")
            startAnimation(GraphUtil.ANIMATION_DURATION_LONG)
        }

    private var totalDegree = 180.0
    private var prevAmount:Double = 0.0
    private var currentAmount:Double = 0.0
    private var startAngle:Float = -180.0f
    private var centerX = 0.0f
    private var centerY = 0.0f
    private lateinit var paints:ArrayList<Paint>

    private lateinit var rectF:RectF
    private lateinit var camera:Camera

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


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var start = 0.0
        var sum = 0.0
        camera.save()
        canvas?.save()
        canvas?.translate(centerX, centerY)
        camera.applyToCanvas(canvas)
        canvas?.translate(-centerX, -centerY)
        canvas?.rotate(startAngle, centerX, centerY)
        amount.forEachIndexed { idx, value ->
            var v = sum + value
            val end = if ( v >  currentAmount ) currentAmount else v
            if( end <= start) return
            if(idx >= paints.size ) return
            val paint = paints[ idx ]
            val s = start.toFloat()
            val e = end.toFloat() - s
            canvas?.drawArc(rectF,s, e, true, paint)
            start = end
            sum += value
        }
        canvas?.restore()
        camera.restore()
        if( currentAmount != totalDegree ) return
        delegate?.let {
            val data = ArrayList<Pair<Double, Point>>()
            val l = rectF.width()/2.0f
            sum = startAngle.toDouble()
            amount.forEach { value ->
                val v = sum + (value/2.0)
                val r = v * Math.PI/180
                val tx = centerX + (Math.cos(r) *l)
                val ty = centerY + (Math.sin(r) *l)
                val point = Point( Math.round(tx).toInt() , Math.round(ty).toInt() )
                data.add(Pair(value, point))
                sum += value
            }
            it.drawGraph(this, data)
        }
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
        currentAmount = GraphUtil.easeOutSine(currentTime.toDouble(), 0.0 , totalDegree , duration.toDouble())
    }

    override fun onCompleted(f: Int) {
        currentAmount =  totalDegree
    }

}