package com.kakaovx.homet.user.component.ui.skeleton.view
import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.module.ComponentAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.injecter.RecyclerFrameLayout
import kotlinx.android.synthetic.main.ui_recycler_list.view.*
import javax.inject.Inject

class ComponentList: RecyclerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    @Inject lateinit var viewAdapter: ComponentAdapter
    @Inject lateinit var viewManager: VerticalLinearLayoutManager

    override fun onCreated() {
        component.inject(this@ComponentList)

        viewAdapter.delegate = this
        recyclerView.layoutManager = viewManager
        recyclerView.adapter = viewAdapter
        viewAdapter.setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }

}