package com.kakaovx.homet.lib.page

import android.view.View
import androidx.annotation.CallSuper
import kotlinx.android.synthetic.main.popup_test.*

abstract class PageDividedGestureFragment:PageGestureFragment() {

    private lateinit var dividedView:View
    protected var positionOffset = 0f
    abstract fun getDividedView(): View

    @CallSuper
    override fun onCreated() {
        super.onCreated()
        dividedView = getDividedView()
        dividedView.alpha = 0f
        dividedView.viewTreeObserver.addOnGlobalLayoutListener { onDividedView() }
    }

    protected fun onDividedView() {
        positionOffset = dividedView.translationY
    }

    override fun onMove(view: PageGestureView, pct:Float) {
        super.onMove(view, pct)
        moveGestureArea(pct)
    }
    override fun onAnimate(view: PageGestureView, pct: Float) {
        moveGestureArea(pct)
    }

    protected open fun moveGestureArea(pct:Float) {
        dividedView.alpha = pct
        dividedView.translationY = contents.translationY + positionOffset
    }



}