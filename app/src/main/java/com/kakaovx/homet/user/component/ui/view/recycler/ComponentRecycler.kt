package com.kakaovx.homet.user.component.ui.view.recycler

import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.network.RxObservableConverter
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.network.model.ApiResponse
import com.kakaovx.homet.user.component.ui.module.ComponentAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.view.RecyclerFrameLayout
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.ui_recycler.view.*

class ComponentRecycler: RecyclerFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    private val TAG = javaClass.simpleName

    override fun onCreated() {

        recyclerView.layoutManager =  VerticalLinearLayoutManager(context)
        recyclerView.adapter = ComponentAdapter().setDatas(arrayOf("AAA","BBB","CCC","DDD","EEE","FFF","AAA","BBB","CCC","DDD","EEE","FFF"))
    }

    override fun injectApi(api: RestfulApi) {

        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "apple"
        api.searchRepositories(params)

        RxObservableConverter.forNetwork(api.searchRepositories(params))
            .subscribe(
                this::handleComplete,
                this::handleError
            ).apply { disposables?.add(this) }
    }

    private fun handleComplete(data: ApiResponse) {
        Log.i(TAG, "handleComplete ($data)")
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }
}