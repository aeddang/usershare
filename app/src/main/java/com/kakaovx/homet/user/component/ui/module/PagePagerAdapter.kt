package com.kakaovx.homet.user.component.ui.module

import android.support.v4.app.FragmentManager
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BasePagePagerAdapter
import com.kakaovx.homet.user.ui.PageFactory
import com.kakaovx.homet.user.ui.PageID

class PagePagerAdapter(fragmentManager: FragmentManager?): BasePagePagerAdapter<PageID>(fragmentManager) {
    override fun getPageFragment(position: Int): PageFragment {
        return PageFactory.getInstence().getPageByID(pages[position])
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].title
    }
}
