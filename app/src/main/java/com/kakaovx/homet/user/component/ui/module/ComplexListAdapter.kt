package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.view.item.CardItem
import com.kakaovx.homet.user.component.ui.view.item.ImageItem
import com.kakaovx.homet.user.component.ui.view.item.LandscapeListItem
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class ComplexListAdapter: MultipleAdapter<ResultData>() {

    private val TAG = javaClass.simpleName

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        Log.d(TAG, "getViewHolder() viewType[$viewType]")
        return when(viewType) {
            AppConst.HOMET_LIST_ITEM_HOME_PROGRAM -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_PROGRAM")
                ViewHolder(LandscapeListItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_WORKOUT_TYPE -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_WORKOUT_TYPE")
                ViewHolder(CardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_BANNER -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_BANNER")
                ViewHolder(CardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_FREE_WORKOUT")
                ViewHolder(CardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_TRAINER -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_TRAINER")
                ViewHolder(CardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_HASH_TAG -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_HASH_TAG")
                ViewHolder(LandscapeListItem(parent.context))
            }
            else -> { ViewHolder(ImageItem(parent.context)) }
        }
    }

    override fun getViewType(position: Int): Int {
        return position or AppConst.HOMET_LIST_ITEM_HOME_INDEX
    }
}