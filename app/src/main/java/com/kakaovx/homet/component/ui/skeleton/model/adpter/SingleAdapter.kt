package com.kakaovx.homet.component.ui.skeleton.model.adpter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.kakaovx.homet.component.ui.skeleton.view.ListCell

abstract class SingleAdapter<T>(pageSize:Int,isViewMore:Boolean = false) : BaseAdapter<T>(pageSize, isViewMore) {
    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BaseAdapter.ViewHolder {
        return ViewHolder(getListCell(parent))
    }
}