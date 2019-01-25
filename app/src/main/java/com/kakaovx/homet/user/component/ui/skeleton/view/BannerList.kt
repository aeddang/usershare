package com.kakaovx.homet.user.component.ui.skeleton.view
import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.skeleton.injecter.RecyclerFrameLayout
import com.kakaovx.homet.user.component.ui.module.BannerAdapter
import com.kakaovx.homet.user.component.ui.module.HorizontalLinearLayoutManager
import kotlinx.android.synthetic.main.ui_recycler_list.view.*
import javax.inject.Inject

class BannerList: RecyclerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    @Inject lateinit var viewAdapter: BannerAdapter
    @Inject lateinit var viewManager: HorizontalLinearLayoutManager

    override fun onCreated() {
        component.inject(this@BannerList)
        viewAdapter.delegate = this
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
        viewAdapter.setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }

    override fun viewMore( page:Int, size:Int) {
        super.viewMore(page, size)
        viewAdapter.viewMoreComplete(arrayOf("AAA"+page.toString(),"BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }
}