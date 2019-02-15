package com.kakaovx.homet.user.component.ui.skeleton.model.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

abstract class BaseViewPagerAdapter<V:View,T>: PagerAdapter() {

    lateinit var pages: Array<T>

    abstract fun getPageView(container: ViewGroup, position: Int):V

    fun setData(data:Array<T>):PagerAdapter {
        pages = data
        notifyDataSetChanged()
        return this
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
        //super.destroyItem(container, position, `object`)
    }
    override fun instantiateItem(container: ViewGroup, position: Int):V {
        val v = getPageView(container, position)
        container.addView(v)
        return v
    }

    override fun isViewFromObject(view: View, key: Any): Boolean {
        return view == key
    }

    override fun getCount(): Int {
        return pages.size
    }
}