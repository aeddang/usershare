package com.kakaovx.homet.component.ui.skeleton.model.adapter
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import lib.page.PageFragment

abstract class BasePagePagerAdapter<T>(fragmentManager: FragmentManager, private val pages: ArrayList<T>): FragmentPagerAdapter(fragmentManager) {

    abstract fun getPageFragment(position: Int):PageFragment

    override fun getItem(position: Int): PageFragment {
        return getPageFragment(position)
    }

    override fun getCount(): Int {
        return pages.size
    }
}