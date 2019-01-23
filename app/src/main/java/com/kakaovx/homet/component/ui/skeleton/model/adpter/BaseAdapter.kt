package com.kakaovx.homet.component.ui.skeleton.model.adpter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.kakaovx.homet.component.ui.skeleton.view.ListCell

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.ViewHolder>() {

    lateinit  var datas:Array<T>

    class ViewHolder(val cell: ListCell) : RecyclerView.ViewHolder(cell)

    abstract fun getListCell(parent: ViewGroup): ListCell

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BaseAdapter.ViewHolder {
        return ViewHolder(getListCell(parent))
    }

    @CallSuper
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cell.setData(datas[position])
    }

    override fun getItemCount() = datas.size
}