package com.kakaovx.homet.user.component.ui.view.viewpager

import android.content.Context
import android.util.AttributeSet

import com.kakaovx.homet.user.component.ui.skeleton.injecter.ViewPagerFrameLayout
import com.kakaovx.homet.user.component.ui.module.BannerPagerAdapter
import kotlinx.android.synthetic.main.ui_viewpager.view.*
import javax.inject.Inject

class BannerViewPager: ViewPagerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    @Inject lateinit var viewAdapter: BannerPagerAdapter

    override fun onCreated() {
        component.inject(this@BannerViewPager)
        viewPager.adapter = viewAdapter.setDatas(arrayOf("0","1","2","3"))
    }

    override fun < Array > setData(datas:Array) {

    }
}