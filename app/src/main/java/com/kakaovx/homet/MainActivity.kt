package com.kakaovx.homet

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.kakaovx.homet.page.PageMain
import com.kakaovx.homet.page.PageNetworkTest
import lib.page.PageActivity
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import lib.page.PageFragment

class MainActivity : PageActivity<PageID>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pageArea = findViewById<ViewGroup>(R.id.pageArea) as ViewGroup?
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