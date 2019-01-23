package com.kakaovx.homet.component.ui.skeleton.injecter
import android.content.Context
import android.support.annotation.CallSuper
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.reactivex.disposables.CompositeDisposable
import lib.page.Page

abstract class InjectableLinearLayout : LinearLayout, Injectable, Page {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    protected var disposables: CompositeDisposable? = null

    init {
        View.inflate(context, getLayoutResId(), this)
        inject()
        onCreated()
    }

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        disposables = CompositeDisposable()
        onAttached()
        onSubscribe()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables?.clear()
        disposables = null
        onDetached()
    }

    final override fun onDetached() {}
}