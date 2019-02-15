package com.kakaovx.homet.user.component.ui.module

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BasePageStatePagerAdapter
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.PageFactory
import com.kakaovx.homet.user.ui.PageID

class PagePagerAdapter(fragmentManager: FragmentManager,
                       val context: Context?): BasePageStatePagerAdapter<PageID>(fragmentManager) {

    override fun getPageFragment(position: Int): PageFragment {
        return PageFactory.getInstance().getPageByID(pages[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context?.getString(pages[position].resId) ?: AppConst.HOMET_VALUE_NOTITLE
    }
}
