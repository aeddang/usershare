package com.kakaovx.homet.component.ui.skeleton.model.adpter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.kakaovx.homet.component.ui.skeleton.view.ListCell

abstract class MultipleAdapter<T> (private val datas: Array<T>) : RecyclerView.Adapter<MultipleAdapter.ViewHolder>() {

    class ViewHolder(val cell: ListCell) : RecyclerView.ViewHolder(cell)

    abstract fun getViewHolder( viewType: Int): MultipleAdapter.ViewHolder
    abstract fun getViewType( position: Int): Int

    @CallSuper
    override fun getItemViewType(position: Int): Int {
        return getViewType(position)
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  MultipleAdapter.ViewHolder {
        return getViewHolder(viewType)
    }

    @CallSuper
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cell.setData(datas[position])
    }
    override fun getItemCount() = datas.size
}