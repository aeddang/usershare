package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.view.item.CardListItem
import com.kakaovx.homet.user.component.ui.view.item.ImageCardListItem
import com.kakaovx.homet.user.component.ui.view.item.ImageListItem
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class ContentListAdapter(private val listType: Int): MultipleAdapter<ResultData>() {

    private val TAG = javaClass.simpleName

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        Log.d(TAG, "getViewHolder() viewType[$viewType]")
        return when(viewType) {
            AppConst.HOMET_LIST_ITEM_PROGRAM -> { ViewHolder(CardListItem(parent.context)) }
            AppConst.HOMET_LIST_ITEM_WORKOUT -> { ViewHolder(CardListItem(parent.context)) }
            AppConst.HOMET_LIST_ITEM_FREE_WORKOUT -> { ViewHolder(CardListItem(parent.context)) }
            AppConst.HOMET_LIST_ITEM_TRAINER -> { ViewHolder(ImageCardListItem(parent.context)) }
            else -> { ViewHolder(ImageListItem(parent.context)) }
        }
    }

    override fun getViewType(position: Int): Int {
        return listType
    }
}