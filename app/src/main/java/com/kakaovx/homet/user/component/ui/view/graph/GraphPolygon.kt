package com.kakaovx.homet.user.component.ui.view.graph

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.skeleton.view.AnimatedDrawView
import com.kakaovx.homet.user.util.Log


class GraphPolygon@kotlin.jvm.JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :  AnimatedDrawView(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName
    var total:Double = 0.0
    var amount:List<Double> = ArrayList()
        set(value) {
            if(value.isEmpty()) return
            field = value
            startAnimation(GraphUtil.ANIMATION_DURATION_LONG)
        }


    private var currentAmount:Double = 0.0
    private var startAngle:Float = -90.0f
    private var changeAngle:Float = -0.0f
    private var centerX = 0.0f
    private var centerY = 0.0f
    private var maxValue = 0.0f


    private lateinit var paint:Paint

    private lateinit var camera:Camera
    private var isApplyCamera:Boolean = false

    fun initSet(totalAmount:Double, color:String) {
        paint = Paint()
        paint.color = Color.parseColor( color )
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.SQUARE
        paint.strokeWidth = 2.0f
        total = totalAmount
        camera = Camera()
        //camera.rotateY(-40.0f)
        //camera.rotateX(20.0f)
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var rotate = 0.0f
        var prevX = 0.0
        var prevY = 0.0
        var initX = 0.0
        var initY = 0.0
        camera.save()
        canvas?.save()
        canvas?.translate(centerX, centerY)
        camera.applyToCanvas(canvas)
        canvas?.translate(-centerX, -centerY)
        canvas?.rotate(startAngle, centerX, centerY)
        amount.forEachIndexed { idx, value ->
            val v = maxValue * value / total * currentAmount
            val r = rotate * Math.PI/180
            val tx = centerX + (Math.cos(r) *v)
            val ty = centerY + (Math.sin(r) *v)
            rotate += changeAngle
            if(idx  == 0) {
                canvas?.drawPoint(tx.toFloat(), ty.toFloat(), paint)
                initX = tx
                initY = ty
            }
            else canvas?.drawLine(prevX.toFloat(), prevY.toFloat(), tx.toFloat(), ty.toFloat(), paint)
            prevX = tx
            prevY = ty
        }
        canvas?.drawLine(prevX.toFloat(), prevY.toFloat(), initX.toFloat(), initY.toFloat(), paint)
        canvas?.restore()
        startAngle ++
        camera.restore()
    }

    override fun onStart() {
        maxValue = width.toFloat() / 2.0f * 0.7f
        centerX = width.toFloat()/2.0f
        centerY = height.toFloat()/2.0f
        currentAmount = 0.0
        changeAngle = 360.0f / amount.size
        startAngle = -90.0f
    }

    override fun onCompute(f: Int) {
        currentAmount = GraphUtil.easeOutElastic(currentTime.toDouble(), 0.0 , 1.0 , duration.toDouble())
    }

    override fun onCompleted(f: Int) {
        currentAmount =  1.0
    }

}