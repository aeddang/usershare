package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.AttributeSet
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.injecter.InjectableFrameLayout
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter


abstract class ViewPagerFrameLayout : InjectableFrameLayout, BaseAdapter.Delegate {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun getLayoutResId(): Int {
        return R.layout.ui_viewpager
    }
}