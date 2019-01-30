package com.kakaovx.homet.user.ui.view.recycler

import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.di.page.BannerAdapter
import com.kakaovx.homet.user.component.di.page.HorizontalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.view.RecyclerFrameLayout
import kotlinx.android.synthetic.main.ui_recycler.view.*

class BannerRecycler: RecyclerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    lateinit var viewAdapter: BannerAdapter
    //@Inject lateinit var viewManager: HorizontalLinearLayoutManager

    fun inject(module: BannerAdapter) {
        viewAdapter = module
        viewAdapter.delegate = this
        recyclerView.adapter = viewAdapter.setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }

    fun inject(module: HorizontalLinearLayoutManager) {
        recyclerView.layoutManager = module
    }

    override fun < Array > setData(datas:Array) {

    }

    override fun viewMore( page:Int, size:Int) {
        super.viewMore(page, size)
        viewAdapter.viewMoreComplete(arrayOf("AAA"+page.toString(),"BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }
}