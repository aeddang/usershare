package com.kakaovx.homet.page
import android.support.v4.app.Fragment
import com.kakaovx.homet.PageID
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectablePageFragment
import lib.page.PagePresenter
import kotlinx.android.synthetic.main.page_main.*


class PageMain : InjectablePageFragment()
{
    override fun getLayoutResId(): Int { return R.layout.page_main }

    override fun inject(fragment: Fragment) {

    }
    override fun init() {
        buttonTestA.setOnClickListener{ PagePresenter.getInstence<PageID>()?.showNavigation() }
        //buttonTestA.setOnClickListener{ PagePresenter.getInstence<PageID>()?.pageChange(PageID.SUB) }
        buttonTestB.setOnClickListener{ PagePresenter.getInstence<PageID>()?.openPopup(PageID.POPUP_TEST) }
    }

}