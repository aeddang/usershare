package com.kakaovx.homet.user.component.ui.skeleton.view

import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.ui.ParamType

abstract class ViewPagerPageFragment : RxPageFragment() {

    protected lateinit var pages: Array<PageID>

    override fun getLayoutResId(): Int { return R.layout.page_content }

    override fun setParam(param:Map<String, Any>): PageFragment {
        pages = param[ParamType.PAGES.key] as Array<PageID>
        return this
    }
}