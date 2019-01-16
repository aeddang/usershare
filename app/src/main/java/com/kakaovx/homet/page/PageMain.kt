package com.kakaovx.homet.page
import android.support.v4.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.PageID
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.InjectablePageFragment
import lib.page.PagePresenter
import kotlinx.android.synthetic.main.page_main.*


class PageMain : InjectablePageFragment()
{
    override fun getLayoutResId(): Int {
        return R.layout.page_main
    }

    override fun inject(fragment: Fragment) {

    }

    override fun init() {
        buttonTestA.clicks()
            .subscribe({ PagePresenter.getInstence<PageID>()?.pageChange(PageID.TEST)})
            .apply { disposables.add(this) }
    }

}