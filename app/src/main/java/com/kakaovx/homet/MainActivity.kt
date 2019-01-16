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

    override fun <T> getPageByID(id:T): PageFragment
    {
        var page: PageFragment
        when(id)
        {
            PageID.MAIN -> { page = PageMain() }
            PageID.TEST -> { page = PageNetworkTest() }
            else -> { page = PageMain() }
        }
        return page
    }


    override fun <T> getPopupByID(id:T): PageFragment
    {
        var page: PageFragment
        when(id)
        {
            PageID.MAIN -> { page = PageMain() }
            PageID.TEST -> { page = PageNetworkTest() }
            else -> { page = PageMain() }
        }
        return page
    }

}
enum class PageID
{
    MAIN,SUB,TEST
}