package com.kakaovx.homet.user.ui.view.viewpager

import android.content.Context
import android.util.AttributeSet

import com.kakaovx.homet.user.component.ui.skeleton.view.ViewPagerFrameLayout
import com.kakaovx.homet.user.component.di.page.BannerPagerAdapter
import kotlinx.android.synthetic.main.ui_viewpager.view.*

class BannerViewPager: ViewPagerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    fun inject(module: BannerPagerAdapter) {
        viewPager.adapter = module.setDatas(arrayOf("0","1","2","3"))
    }

}