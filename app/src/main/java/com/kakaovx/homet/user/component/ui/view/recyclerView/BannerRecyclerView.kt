package com.kakaovx.homet.user.component.ui.view.recyclerView

import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.module.BannerAdapter
import com.kakaovx.homet.user.component.ui.module.HorizontalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.view.RecyclerFrameLayout
import kotlinx.android.synthetic.main.ui_recyclerview.view.*

class BannerRecyclerView: RecyclerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    private lateinit var viewAdapter: BannerAdapter

    override fun onCreated() {
        viewAdapter = BannerAdapter()
        viewAdapter.delegate = this
        recyclerView.layoutManager = HorizontalLinearLayoutManager(context)
        recyclerView.adapter = viewAdapter.setData(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }


    override fun viewMore( page:Int, size:Int) {
        super.viewMore(page, size)
        viewAdapter.viewMoreComplete(arrayOf("AAA"+page.toString(),"BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }
}