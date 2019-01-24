package com.kakaovx.homet.component.ui.skeleton.model.adpter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.Size
import com.kakaovx.homet.component.ui.skeleton.model.data.InfinityPaginationData
import com.kakaovx.homet.component.ui.skeleton.view.ListCell

abstract class BaseAdapter<T>(pageSize:Int,val isViewMore:Boolean = false) : RecyclerView.Adapter<BaseAdapter.ViewHolder>() {
    class ViewHolder(val cell: ListCell) : RecyclerView.ViewHolder(cell)

    private var total = 0
    private var isBusy = false
    protected var paginationData:InfinityPaginationData<T> = InfinityPaginationData(pageSize)

    fun setDatas(datas:Array<T>) {
        paginationData.reset()
        paginationData.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas:Array<T>) {
        paginationData.addAll(datas)
        notifyItemRangeInserted(paginationData.datas.size, datas.size)
    }

    fun insertData(data:T,idx:Int = -1) {
        val position = if (idx == -1) paginationData.datas.size else idx
        if(position == -1 || position >= total) return
        paginationData.datas.add(position,data)
        notifyItemInserted(position)
    }

    fun updateData(data:T,idx:Int) {
        if(idx == -1 || idx >= total) return
        paginationData.datas[idx] = data
        notifyItemChanged(idx)
    }

    fun removeData(data:T) {
        val position = paginationData.datas.indexOf(data)
        removeData(position)
    }

    fun removeData(idx:Int) {
        if(idx == -1 || idx >= total) return
        paginationData.datas.removeAt(idx)
        notifyItemRemoved(idx)
    }

    fun removeAllDatas() {
        paginationData.reset()
        notifyDataSetChanged()
    }

    abstract fun getListCell(parent: ViewGroup): ListCell
    protected fun viewMore(adapter:BaseAdapter<T>, page:Int, size:Int){}
    @CallSuper
    protected fun viewMoreComplete(datas:Array<T>) {
        addDatas(datas)
        isBusy = false
    }

    override fun getItemCount():Int {
        total = paginationData.datas.size
        return total
    }

    @CallSuper
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cell.setData(paginationData.datas[position])
        if(position == total && isViewMore && paginationData.isPageable && !isBusy) {
            isBusy = true
            viewMore(this, paginationData.currentPage, paginationData.pageSize)
        }
    }
}