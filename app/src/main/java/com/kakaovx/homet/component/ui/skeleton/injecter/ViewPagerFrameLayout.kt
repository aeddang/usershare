package com.kakaovx.homet.component.ui.skeleton.injecter

import android.content.Context
import android.support.annotation.CallSuper
import android.util.AttributeSet
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.component.DaggerViewPagerComponent
import com.kakaovx.homet.component.ui.component.ViewPagerComponent
import com.kakaovx.homet.component.ui.module.*
import com.kakaovx.homet.component.ui.skeleton.model.adapter.BaseAdapter

abstract class ViewPagerFrameLayout : InjectableFrameLayout, BaseAdapter.Delegate {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    lateinit var component: ViewPagerComponent

    override fun getLayoutResId(): Int {
        return R.layout.ui_viewpager
    }

    @CallSuper
    override fun inject() {
        component = DaggerViewPagerComponent.builder()
            .pagerAdapterModule(PagerAdapterModule())
            .build()
    }

}