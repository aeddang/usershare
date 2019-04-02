package com.kakaovx.homet.user.ui.page

import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.PageID
import kotlinx.android.synthetic.main.page_test.*

class PageTest : RxPageFragment() {

    override fun getLayoutResId(): Int { return R.layout.page_test }

    override fun onSubscribe() {
        super.onSubscribe()
        btnTest1.clicks().subscribe(this::onTest1).apply { disposables.add(this) }
        btnTest2.clicks().subscribe(this::onTest2).apply { disposables.add(this) }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onTest1(v: Unit) {
       // PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_LOGIN, null, image, "testAni")
        PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_LOGIN)
        //PagePresenter.getInstance<PageID>().pageChange(PageID.PROGRAM_REPORT)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onTest2(v: Unit) {
        PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_PLAYER)
    }



}