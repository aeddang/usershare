package com.kakaovx.homet.user.component.ui.skeleton.model.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kakaovx.homet.lib.page.PageFragment

abstract class BasePagePagerAdapter<T>(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    lateinit var pages: Array<T>

    abstract fun getPageFragment(position: Int): PageFragment

    fun setDatas(datas:Array<T>): FragmentPagerAdapter {
        pages = datas
        notifyDataSetChanged()
        return this
    }

    override fun getItem(position: Int): PageFragment {
        return getPageFragment(position)
    }

    override fun getCount(): Int {
        return pages.size
    }
}