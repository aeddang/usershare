package com.kakaovx.homet.component.ui.skeleton.model.adapter
import android.view.ViewGroup
import androidx.annotation.CallSuper

abstract class MultipleAdapter<T>(isViewMore:Boolean = false, pageSize:Int = -1): BaseAdapter<T>(isViewMore, pageSize) {

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder
    abstract fun getViewType( position: Int): Int

    @CallSuper
    override fun getItemViewType(position: Int): Int {
        return getViewType(position)
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BaseAdapter.ViewHolder {
        return getViewHolder(parent, viewType)
    }
}