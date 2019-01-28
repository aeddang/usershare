package com.kakaovx.homet.user.component.ui.skeleton.model.adapter

import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem

abstract class SingleAdapter<T>(isViewMore:Boolean = false, pageSize:Int = -1): BaseAdapter<T>(isViewMore, pageSize) {
    abstract fun getListCell(parent: ViewGroup): ListItem
    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BaseAdapter.ViewHolder {
        return ViewHolder(getListCell(parent))
    }
}