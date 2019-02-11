package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.view.item.BannerListItem
import com.kakaovx.homet.user.component.ui.view.item.ImageListItem
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class ContentListAdapter(private val listType: Int): MultipleAdapter<String>() {

    private val TAG = javaClass.simpleName

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        return when(viewType) {
            AppConst.HOMET_LIST_ITEM_PROGRAM -> { ViewHolder(ImageListItem(parent.context)) }
            AppConst.HOMET_LIST_ITEM_WORKOUT -> { ViewHolder(BannerListItem(parent.context)) }
            AppConst.HOMET_LIST_ITEM_FREE_WORKOUT -> { ViewHolder(BannerListItem(parent.context)) }
            AppConst.HOMET_LIST_ITEM_TRAINER -> { ViewHolder(ImageListItem(parent.context)) }
            else -> { ViewHolder(ImageListItem(parent.context)) }
        }
    }

    override fun getViewType(position: Int): Int {
        Log.d(TAG, "position = $position")
        return listType
    }
}