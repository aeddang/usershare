package lib.page

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import lib.ui.Gesture


abstract class PageGestureView : FrameLayout, Gesture.Delegate
{
    var closeType = Gesture.Type.PAN_DOWN
    var delegate: Delegate? = null
    var contentsView:View? = null

    private var isVertical = false
    private var isHorizontal = false
    private var gesture:Gesture? = null
    private var startPosition = 0f
    private var finalGesture = Gesture.Type.NONE
    private var animationCloseRunnable: Runnable = Runnable {didCloseAnimation()}
    private var animationReturnRunnable: Runnable = Runnable {didReturnAnimation()}

    constructor(context: Context) : super(context){}
    constructor(context: Context, attrs:AttributeSet) : super(context,attrs){}

    fun willCreateAnimation(startPos:Float)
    {
        if(isVertical) contentsView?.translationY = startPos else contentsView?.translationX = startPos
    }

    override fun onAttachedToWindow()
    {
        super.onAttachedToWindow()
        isVertical = if(closeType == Gesture.Type.PAN_UP || closeType == Gesture.Type.PAN_DOWN) true else false
        isHorizontal = if(closeType == Gesture.Type.PAN_LEFT || closeType == Gesture.Type.PAN_RIGHT) true else false
        gesture = Gesture(this,isVertical,isHorizontal)

    }

    override fun onDetachedFromWindow()
    {
        super.onDetachedFromWindow()
        contentsView = null
        delegate = null
        gesture?.onDestroy()
        gesture = null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        gesture?.let {return it.adjustEvent(event)}
        return super.onTouchEvent(event)
    }


    override fun stateChange(g: Gesture, e: Gesture.Type)
    {
        val d = g.changePosA[0]
        when (e)
        {
            Gesture.Type.START -> touchStart()
            Gesture.Type.MOVE_V -> if(isVertical) touchMove(d.y)
            Gesture.Type.MOVE_H -> if(isHorizontal) touchMove(d.x)
            Gesture.Type.END,Gesture.Type.CANCLE -> touchEnd()
        }
    }

    private fun touchStart()
    {
        finalGesture = Gesture.Type.NONE
        contentsView?.let {startPosition = if(isVertical) it.translationY else it.translationX }
    }

    private fun touchMove(delta:Int)
    {
        var p = delta + startPosition
        var max = 0
        when(closeType)
        {
            Gesture.Type.PAN_DOWN ->
            {
                max = height
                if (p > max) p = max.toFloat() else if (p < 0f) p = 0f
                contentsView?.translationY = Math.floor(p.toDouble()).toFloat()
            }
            Gesture.Type.PAN_UP ->
            {
                max = -height
                if (p < max) p = max.toFloat() else if (p > 0f) p = 0f
                contentsView?.translationY = Math.floor(p.toDouble()).toFloat()
            }

            Gesture.Type.PAN_RIGHT ->
            {
                max = width
                if (p > max) p = max.toFloat() else if (p < 0f) p = 0f
                contentsView?.translationX = Math.floor(p.toDouble()).toFloat()
            }
            Gesture.Type.PAN_LEFT ->
            {
                max = width
                if (p < max) p = max.toFloat() else if (p > 0f) p = 0f
                contentsView?.translationX = Math.floor(p.toDouble()).toFloat()
            }

        }
        val pct = (max - p) / max
       // alpha = pct
        delegate?.onMove(this,pct)
    }

    private fun touchEnd()
    {
        if (finalGesture == closeType) onGestureClose() else onGestureReturn()
    }

    override fun gestureComplete(g: Gesture, e: Gesture.Type)
    {
        this.finalGesture = e
    }

    open fun onGestureClose(isClosure:Boolean = true):Long
    {
        var closePosX = 0f
        var closePosY = 0f
        var duration = 100L;
        when(closeType)
        {
            Gesture.Type.PAN_DOWN -> closePosY = height.toFloat()
            Gesture.Type.PAN_UP -> closePosY = -height.toFloat()
            Gesture.Type.PAN_RIGHT -> closePosX = width.toFloat()
            Gesture.Type.PAN_LEFT -> closePosX = -width.toFloat()
        }

        contentsView?.let {
            duration = if(closePosY != 0f) Math.abs(closePosY - it.translationY).toLong() else Math.abs(closePosX - it.translationX).toLong()
            duration = duration/5
            val ani =
                    it.animate()
                    .translationX(closePosX)
                    .translationY(closePosY)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(duration)
            if(isClosure) ani.withEndAction(animationCloseRunnable)
            ani.start()
            }

        return duration
    }
    open protected fun didCloseAnimation()
    {
        delegate?.onClose(this)
    }

    open fun onGestureReturn(isClosure:Boolean = true):Long
    {
        var duration = 100L;

        contentsView?.let {
            duration = if (isVertical) Math.abs(it.translationY).toLong() else Math.abs(it.translationX).toLong()
            duration = duration / 5
            val ani = it.animate()
                    .translationX(0f)
                    .translationY(0f)
                    .setInterpolator(AccelerateInterpolator())
                    .setDuration(duration)
            if (isClosure) ani.withEndAction(animationReturnRunnable)
            ani.start()
        }
        return duration
    }

    open protected fun didReturnAnimation()
    {
        delegate?.onReturn(this)
    }

    interface Delegate
    {
        fun onMove(view: PageGestureView, pct:Float){}
        fun onClose(view: PageGestureView){}
        fun onReturn(view: PageGestureView){}
    }


}