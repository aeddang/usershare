package com.kakaovx.homet.user.component.ui.skeleton.model.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.kakaovx.homet.lib.page.PageFragment

abstract class BasePageStatePagerAdapter<T>(fragmentManager: FragmentManager, private val pages: ArrayList<T>): FragmentStatePagerAdapter(fragmentManager) {

    abstract fun getPageFragment(position: Int): PageFragment

    override fun getItem(position: Int): PageFragment {
        return getPageFragment(position)
    }

    override fun getCount(): Int {
        return pages.size
    }
}