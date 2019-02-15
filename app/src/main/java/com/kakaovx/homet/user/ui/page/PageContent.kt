package com.kakaovx.homet.user.ui.page

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.ViewPagerPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.PageFactory
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.page_viewpager.*

class PageContent : ViewPagerPageFragment() {

    private val TAG = javaClass.simpleName

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_content)
                setDisplayShowTitleEnabled(true)
            }
        }
    }

    override fun onCreated() {
        super.onCreated()
        Log.d(TAG, "onCreated()")

        context?.let { initView(it) }
        activity?.let {
            val adapt = PageViewPagerAdapter(childFragmentManager, context).setDataArray(pages)
            viewPager.adapter = adapt
            recyclerTabLayout.setUpWithViewPager(viewPager)
            viewPager.adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        super.onDestroyed()
    }

    private class PageViewPagerAdapter(fragmentManager: FragmentManager,
                                       val context: Context?): FragmentStatePagerAdapter(fragmentManager) {

        lateinit var pages: Array<PageID>

        fun setDataArray(dataArray:Array<PageID>): PagerAdapter {
            pages = dataArray
            notifyDataSetChanged()
            return this
        }

        fun getPageFragment(position: Int): PageFragment {
            return PageFactory.getInstance().getPageByID(pages[position])
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context?.getString(pages[position].resId) ?: AppConst.HOMET_VALUE_NOTITLE
        }

        override fun getItem(position: Int): PageFragment {
            return getPageFragment(position)
        }

        override fun getCount(): Int {
            return pages.size
        }
    }
}


