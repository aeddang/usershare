package com.kakaovx.homet.component.ui.skeleton.model.adapter

import android.view.ViewGroup
import androidx.annotation.CallSuper

abstract class SingleAdapter<T>(pageSize:Int,isViewMore:Boolean = false) : BaseAdapter<T>(pageSize, isViewMore) {
    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BaseAdapter.ViewHolder {
        return ViewHolder(getListCell(parent))
    }
}