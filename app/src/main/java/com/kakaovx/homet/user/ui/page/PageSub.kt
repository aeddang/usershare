package com.kakaovx.homet.user.ui.page

import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.ui.PageID
import kotlinx.android.synthetic.main.page_sub.*


class PageSub : RxPageFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.page_sub }


    override fun onCreated() {
        super.onCreated()
        buttonBack.setOnClickListener{ PagePresenter.getInstence<PageID>().onBack() }
    }
}