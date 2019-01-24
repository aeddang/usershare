package com.kakaovx.homet.page
import com.kakaovx.homet.PageID
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectablePageFragment
import lib.page.PagePresenter
import kotlinx.android.synthetic.main.page_main.*


class PageMain : InjectablePageFragment()
{
    override fun getLayoutResId(): Int { return R.layout.page_main }

    override fun inject() {

    }

    override fun onCreated() {
        super.onCreated()
        buttonTestA.setOnClickListener{ PagePresenter.getInstence<PageID>()?.pageChange(PageID.MAIN) }
        buttonTestB.setOnClickListener{ PagePresenter.getInstence<PageID>()?.pageChange(PageID.SUB) }
        buttonTestC.setOnClickListener{ PagePresenter.getInstence<PageID>()?.openPopup(PageID.POPUP_TEST) }
    }

    override fun onDestroied() {
        super.onDestroied()
    }

}