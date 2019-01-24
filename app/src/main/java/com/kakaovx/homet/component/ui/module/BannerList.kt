package com.kakaovx.homet.component.ui.module
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectableFrameLayout
import kotlinx.android.synthetic.main.ui_holizontal_list.view.*
import javax.inject.Inject

class BannerList: InjectableFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    @Inject
    lateinit var viewAdapter:BannerAdapter

    override fun inject() {
        DaggerAdapterComponent.builder()
            .dataModule(DataModule())
            .adapterModule(AdapterModule())
            .build().inject(this@BannerList)
    }

    override fun getLayoutResId(): Int {
        return R.layout.ui_holizontal_list
    }

    override fun onCreated() {
        viewAdapter.setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF"))
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = viewAdapter
    }
}