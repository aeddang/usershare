package com.kakaovx.homet.user.page

import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.injecter.InjectablePageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.ui.main.PageID
import kotlinx.android.synthetic.main.page_sub.*


class PageSub : InjectablePageFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.page_sub }

    override fun inject() {

    }

    override fun onCreated() {
        super.onCreated()
        buttonBack.setOnClickListener{ PagePresenter.getInstence<PageID>()?.onBack() }
    }

    override fun onDestroied() {
        super.onDestroied()
    }

}