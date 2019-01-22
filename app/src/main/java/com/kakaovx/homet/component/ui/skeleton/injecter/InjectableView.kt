package com.kakaovx.homet.component.ui.skeleton.injecter
import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

abstract class InjectableView : ViewGroup {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        View.inflate(context, getLayoutResId(), this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}

    protected open fun inject(){}

    @LayoutRes
    abstract fun getLayoutResId(): Int

    @CallSuper
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        inject()
    }
}