package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.item_card.view.*

class CardListItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreated() { }

    override fun getLayoutResId(): Int {
        return R.layout.item_card
    }

    override fun <T> setData(data: T) {
        val resultData: ResultData = data as ResultData
        Log.d(TAG, "setData() get data = [$resultData]")
        card_name.text = resultData.name ?: "card name null"
        card_description.text = resultData.description ?: "card description null"
    }
}