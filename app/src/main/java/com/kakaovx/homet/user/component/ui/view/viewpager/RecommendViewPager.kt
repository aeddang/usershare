package com.kakaovx.homet.user.component.ui.view.viewpager

import android.content.Context
import android.util.AttributeSet

import com.kakaovx.homet.user.component.ui.skeleton.view.ViewPagerFrameLayout
import com.kakaovx.homet.user.component.ui.module.BannerPagerAdapter
import kotlinx.android.synthetic.main.ui_viewpager.view.*

class RecommendViewPager: ViewPagerFrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun onCreated() {
        viewPager.adapter = BannerPagerAdapter().setData(arrayOf("0","1","2"))
    }

    override fun onDestroyed() {}
}