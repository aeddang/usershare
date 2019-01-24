package com.kakaovx.homet.component.ui
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.module.AdapterModule
import com.kakaovx.homet.component.ui.module.BannerAdapter
import com.kakaovx.homet.component.ui.module.DaggerAdapterComponent
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectableFrameLayout
import com.kakaovx.homet.component.ui.skeleton.model.adpter.BaseAdapter
import kotlinx.android.synthetic.main.ui_holizontal_list.view.*
import javax.inject.Inject

class BannerList: InjectableFrameLayout, BaseAdapter.Delegate {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    @Inject
    lateinit var viewAdapter: BannerAdapter

    override fun inject() {
        DaggerAdapterComponent.builder()
            .adapterModule(AdapterModule())
            .build().inject(this@BannerList)
    }

    override fun getLayoutResId(): Int {
        return R.layout.ui_holizontal_list
    }

    override fun onCreated() {
        viewAdapter.delegate = this
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = viewAdapter

        viewAdapter.setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF"))
    }
    override fun viewMore( page:Int, size:Int){

    }
}