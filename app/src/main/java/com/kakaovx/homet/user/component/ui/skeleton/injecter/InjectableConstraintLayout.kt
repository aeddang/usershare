package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.content.Context
import android.support.annotation.CallSuper
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.kakaovx.homet.lib.page.Page
import io.reactivex.disposables.CompositeDisposable

abstract class InjectableConstraintLayout : ConstraintLayout, Injectable, Page {
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

    final override fun onDestroied() {}

}