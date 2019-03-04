package com.kakaovx.homet.user.component.ui.view.recyclerView

import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.ui.module.ComponentAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.view.RecyclerFrameLayout
import kotlinx.android.synthetic.main.ui_recyclerview.view.*

class ComponentRecyclerView: RecyclerFrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun onCreated() {
        recyclerView.layoutManager = VerticalLinearLayoutManager(context)
        recyclerView.adapter = ComponentAdapter().setDataArray(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }

    override fun injectApi(api: RestfulApi) {
        return
    }

    override fun onDestroyed() {}
}