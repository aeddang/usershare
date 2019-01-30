package com.kakaovx.homet.user.ui.view.recycler

import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.di.page.ComponentAdapter
import com.kakaovx.homet.user.component.di.page.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.view.RecyclerFrameLayout
import kotlinx.android.synthetic.main.ui_recycler.view.*

class ComponentRecycler: RecyclerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    fun inject(module: ComponentAdapter) {
        module.delegate = this
        recyclerView.adapter = module.setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }

    fun inject(module: VerticalLinearLayoutManager) {
        recyclerView.layoutManager = module
    }


}