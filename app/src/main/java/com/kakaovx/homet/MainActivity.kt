package com.kakaovx.homet

import android.view.ViewGroup
import com.kakaovx.homet.page.PageMain
import com.kakaovx.homet.page.PageNetworkTest
import lib.page.PageActivity
import lib.page.PageFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PageActivity<PageID>() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun getPageAreaId(): Int {
        return R.id.area
    }

    override fun init() {
       this.pagePresenter.pageChange(PageID.MAIN)
    }

    override fun <T> getPageByID(id:T): PageFragment?
    {
        var page: PageFragment? = null
        when(id)
        {
            PageID.MAIN -> {page = PageMain()}
            PageID.TEST -> {page = PageNetworkTest()}
        }
        return page?.let {it} ?: super.getPageByID(id)
    }

    /*
    override fun <T> getPopupByID(id:T): PageFragment?
    {

        var popup: PageFragment? = null
        when(id)
        {
            PageID.TEST -> {popup = PopupTest()}
        }

        return popup?.let {it} ?: super.getPopupByID(id)
    }
    */
}
enum class PageID
{
    MAIN,SUB,TEST
}