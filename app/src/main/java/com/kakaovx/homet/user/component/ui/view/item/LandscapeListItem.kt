package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.item_landscape_list.view.*

class LandscapeListItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreated() { }

    override fun getLayoutResId(): Int {
        return R.layout.item_landscape_list
    }

    override fun <T> setData(data: T) {
        val resultData: ResultData = data as ResultData
        Log.d(TAG, "setData() get data = [$resultData]")
        landscapeListTitle.text = resultData.name ?: "title null"
        landscapeListSubTitle.text = resultData.fullName ?: "sub title null"
    }
}