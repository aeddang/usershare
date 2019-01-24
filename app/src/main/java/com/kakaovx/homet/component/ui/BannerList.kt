package com.kakaovx.homet.component.ui
import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.module.*
import com.kakaovx.homet.component.ui.skeleton.injecter.InjectableFrameLayout
import com.kakaovx.homet.component.ui.skeleton.model.adapter.BaseAdapter
import kotlinx.android.synthetic.main.ui_holizontal_list.view.*
import javax.inject.Inject

class BannerList: InjectableFrameLayout, BaseAdapter.Delegate {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    @Inject lateinit var viewAdapter: BannerAdapter
    @Inject lateinit var viewManager: HorizontalLinearLayoutManager

    override fun inject() {
        DaggerListComponent.builder()
            .adapterModule(AdapterModule())
            .layoutManagerModule(LayoutManagerModule(context))
            .build().inject(this@BannerList)
    }

    override fun getLayoutResId(): Int {
        return R.layout.ui_holizontal_list
    }

    override fun onCreated() {
        viewAdapter.delegate = this
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
        viewAdapter.setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }

    override fun viewMore( page:Int, size:Int){
        viewAdapter.viewMoreComplete(arrayOf("AAA"+page.toString(),"BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }
}