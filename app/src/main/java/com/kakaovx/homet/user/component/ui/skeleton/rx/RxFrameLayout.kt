package com.kakaovx.homet.user.component.ui.skeleton.rx

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import com.kakaovx.homet.lib.page.Page
import io.reactivex.disposables.CompositeDisposable

abstract class RxFrameLayout : FrameLayout, Rx, Page {

    constructor(context: Context): super(context) { init(context) }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) { init(context) }

    protected var disposables: CompositeDisposable? = null

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(getLayoutResId(), this, true)
    }

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        disposables = CompositeDisposable()
        onCreated()
        onAttached()
        onSubscribe()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables?.clear()
        disposables = null
        onDetached()
        onDestroyed()
    }
}