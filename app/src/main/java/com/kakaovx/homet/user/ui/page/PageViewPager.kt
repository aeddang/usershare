package com.kakaovx.homet.user.ui.page

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.ui.skeleton.view.ViewPagerPageFragment
import com.kakaovx.homet.user.ui.PageFactory
import com.kakaovx.homet.user.ui.PageID
import javax.inject.Inject
import kotlinx.android.synthetic.main.page_viewpager.*


class PageViewPager : ViewPagerPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject lateinit var setting: SettingPreference

    override fun onCreated() {
        super.onCreated()
        activity?.let {
            val adapt = PageViewPagerAdapter(childFragmentManager).setDatas(pages)
            viewPager.adapter = adapt
            recyclerTabLayout.setUpWithViewPager(viewPager)
            viewPager.adapter?.notifyDataSetChanged()
        }

    }

    override fun onDestroyed() {
        super.onDestroyed()
    }

    private class PageViewPagerAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

        lateinit var pages: Array<PageID>

        fun setDatas(datas:Array<PageID>): PagerAdapter {
            pages = datas
            notifyDataSetChanged()
            return this
        }

        fun getPageFragment(position: Int): PageFragment {
            return PageFactory.getInstance().getPageByID(pages[position])
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pages[position].title
        }

        override fun getItem(position: Int): PageFragment {
            return getPageFragment(position)
        }

        override fun getCount(): Int {
            return pages.size
        }
    }
}


