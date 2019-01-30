package com.kakaovx.homet.user.component.ui.skeleton.view

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.injecter.InjectablePageFragment
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.ui.ParamType

abstract class ViewPagerPageFragment : InjectablePageFragment() {

    protected lateinit var pages:Array<PageID>

    override fun getLayoutResId(): Int { return R.layout.page_viewpager }

    override fun setParam(param:Map<String,Any>): PageFragment {
        pages = param[ParamType.PAGES.key] as Array<PageID>
        return this
    }
}