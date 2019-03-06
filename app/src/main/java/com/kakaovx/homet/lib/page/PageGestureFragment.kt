package com.kakaovx.homet.lib.page

import android.view.View
import androidx.annotation.CallSuper

abstract class PageGestureFragment: PageFragment(), PageGestureView.Delegate {

    private lateinit var gestureView: PageGestureView
    private lateinit var contentsView:View
    private lateinit var backgroundView:View

    abstract fun getGestureView(): PageGestureView
    abstract fun getContentsView(): View
    abstract fun getBackgroundView(): View

    @CallSuper
    override fun onCreated() {
        gestureView = getGestureView()
        contentsView = getContentsView()
        backgroundView = getBackgroundView()

        gestureView.delegate = this
        gestureView.contentsView = contentsView

    }

    @CallSuper
    override fun  onDestroyed() {
    }

    override fun onMove(view: PageGestureView, pct:Float) {
        backgroundView.alpha = pct
    }

    override fun onAnimate(view: PageGestureView, pct:Float){
        backgroundView.alpha = pct
    }

    override fun onClose(view: PageGestureView) {
        pageID?.let { PagePresenter.getInstance<Any>().closePopup(it) }

    }

    override fun onReturn(view: PageGestureView) {

    }


}