package com.kakaovx.homet.lib.page

import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.CallSuper
import kotlinx.android.synthetic.main.popup_test.*

abstract class PageDividedGestureFragment: PageGestureFragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var dividedView:View
    private var positionOffset = 0f
    abstract fun getDividedView(): View

    @CallSuper
    override fun onCreated() {
        super.onCreated()
        dividedView = getDividedView()
        dividedView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    @CallSuper
    override fun onDestroyed() {
        super.onDestroyed()
        dividedView.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        dividedView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        onDividedView()
    }

    protected fun onDividedView() {
        positionOffset = dividedView.translationY
    }

    override fun onMove(view: PageGestureView, pct:Float) {
        super.onMove(view, pct)
        moveGestureArea(pct)
    }
    override fun onAnimate(view: PageGestureView, pct: Float) {
        super.onAnimate(view, pct)
        moveGestureArea(pct)
    }

    protected open fun moveGestureArea(pct:Float) {
        dividedView.alpha = pct
        dividedView.translationY = contents.translationY + positionOffset
    }

}