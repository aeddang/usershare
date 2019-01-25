package com.kakaovx.homet.component.ui.view
import android.content.Context
import android.util.AttributeSet

import com.kakaovx.homet.component.ui.module.*
import com.kakaovx.homet.component.ui.skeleton.injecter.ViewPagerFrameLayout
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