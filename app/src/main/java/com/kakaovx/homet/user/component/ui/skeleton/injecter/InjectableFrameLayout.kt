package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.content.Context
import android.support.annotation.CallSuper
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.kakaovx.homet.lib.page.Page
import io.reactivex.disposables.CompositeDisposable

abstract class InjectableFrameLayout : FrameLayout, Inject, Page {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    protected var disposables: CompositeDisposable? = null

    init {
        View.inflate(context, getLayoutResId(), this)
        onCreated()
    }

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        disposables = CompositeDisposable()
        onAttached()
        onSubscribe()
        onInject()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables?.clear()
        disposables = null
        onDetached()
    }

    final override fun onDestroied() {}
}