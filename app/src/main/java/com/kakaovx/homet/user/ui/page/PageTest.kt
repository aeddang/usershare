package com.kakaovx.homet.user.ui.page


import androidx.core.view.ViewCompat
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.PageID
import kotlinx.android.synthetic.main.page_network.*
import kotlinx.android.synthetic.main.page_test.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*

class PageTest : RxPageFragment() {


    override fun onCreated(){
        super.onCreated()
    }

    override fun onSubscribe() {
        super.onSubscribe()
        btnTest.clicks().subscribe(this::onTest).apply { disposables.add(this) }
    }

    fun onTest(v: Unit) {
        PagePresenter.getInstance<PageID>().openPopup(PageID.TEST_POP, null, image, "testAni")
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_test
    }

    override fun onDestroyed() {
        super.onDestroyed()
    }

}