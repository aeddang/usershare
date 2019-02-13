package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.view.item.BannerListItem
import com.kakaovx.homet.user.component.ui.view.item.ImageItem

class ComponentAdapter: MultipleAdapter<String>() {

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        return when(viewType) {
            0 -> { ViewHolder(ImageItem(parent.context)) }
            1 -> { ViewHolder(BannerListItem(parent.context)) }
            else -> { ViewHolder(ImageItem(parent.context)) }
        }
    }

    override fun getViewType(position: Int): Int {
        return position % 2
    }
}