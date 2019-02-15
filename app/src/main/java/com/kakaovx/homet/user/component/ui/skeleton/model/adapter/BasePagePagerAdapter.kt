package com.kakaovx.homet.user.component.ui.skeleton.model.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kakaovx.homet.lib.page.PageFragment

abstract class BasePagePagerAdapter<T>(fragmentManager: FragmentManager?): FragmentPagerAdapter(fragmentManager) {

    lateinit var pages: Array<T>

    abstract fun getPageFragment(position: Int): PageFragment

    fun setData(data:Array<T>): FragmentPagerAdapter {
        pages = data
        notifyDataSetChanged()
        return this
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val fragment = `object` as Fragment?
        fragment?.let {
            container.removeView(it.view)
        }
        super.destroyItem(container, position, `object`)
    }

    override fun getItem(position: Int): PageFragment {
        return getPageFragment(position)
    }

    override fun getCount(): Int {
        return pages.size
    }
}