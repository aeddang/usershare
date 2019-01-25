package com.kakaovx.homet.user.page

import com.kakaovx.homet.user.ui.main.PageID
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.injecter.InjectablePageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import kotlinx.android.synthetic.main.page_main.*


class PageMain : InjectablePageFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int { return R.layout.page_main }

    override fun inject() {

    }

    override fun onCreated() {
        super.onCreated()
        buttonTestA.setOnClickListener{ PagePresenter.getInstence<PageID>()?.pageChange(PageID.TEST) }
        buttonTestB.setOnClickListener{ PagePresenter.getInstence<PageID>()?.pageChange(PageID.SUB) }
        //buttonTestC.setOnClickListener{ PagePresenter.getInstence<PageID>()?.openPopup(PageID.POPUP_TEST) }
    }

    override fun onDestroied() {
        super.onDestroied()
    }

}