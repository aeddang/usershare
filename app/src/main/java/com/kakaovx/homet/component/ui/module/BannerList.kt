package com.kakaovx.homet.component.ui.module
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectableFrameLayout
import kotlinx.android.synthetic.main.ui_holizontal_list.view.*

class BannerList: InjectableFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    private lateinit var viewAdapter: BannerAdapter

    override fun getLayoutResId(): Int {
        return R.layout.ui_holizontal_list
    }

    override fun onCreated() {
        viewAdapter = BannerAdapter()
        viewAdapter.datas = arrayOf("AAA","BBB","CCC","DDD","EEE","FFF")
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewAdapter

    }







}