package com.kakaovx.homet.user.ui.page

import android.support.v4.app.FragmentManager
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R

import com.kakaovx.homet.user.component.ui.skeleton.injecter.InjectablePageFragment
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BasePagePagerAdapter
import com.kakaovx.homet.user.ui.PageFactory
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.ui.ParamType
import kotlinx.android.synthetic.main.page_viewpager.*


class PageViewPager : InjectablePageFragment() {

    private val TAG = javaClass.simpleName
    private lateinit var pages:Array<PageID>
    override fun getLayoutResId(): Int { return R.layout.page_viewpager }
    override open fun setParam(param:Map<String,Any>): PageFragment {
        pages = param[ParamType.PAGES.key] as Array<PageID>
        return this
    }
    override fun inject() {
    }

    override fun onCreated() {
        super.onCreated()
        fragmentManager?.let {
            val adapt = PagePagerAdapter(it).setDatas(pages)
            viewPager.adapter = adapt
        }
    }

    override fun onDestroied() {
        super.onDestroied()
    }

    class PagePagerAdapter(fragmentManager: FragmentManager): BasePagePagerAdapter<PageID>(fragmentManager) {
        override fun getPageFragment(position: Int): PageFragment {
            return PageFactory.getInstence().getPageByID(pages[position])
        }
    }

}


