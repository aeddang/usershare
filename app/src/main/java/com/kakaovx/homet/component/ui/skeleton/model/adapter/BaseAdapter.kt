package com.kakaovx.homet.component.ui.skeleton.model.adapter

import android.os.Handler
import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import com.kakaovx.homet.component.ui.skeleton.model.data.InfinityPaginationData
import com.kakaovx.homet.component.ui.skeleton.view.ListCell

abstract class BaseAdapter<T>(private val isViewMore:Boolean = false, pageSize:Int = -1) : RecyclerView.Adapter<BaseAdapter.ViewHolder>() {
    class ViewHolder(val cell: ListCell) : RecyclerView.ViewHolder(cell)

    var delegate: BaseAdapter.Delegate? = null
    private var viewMoreHandler: Handler = Handler()
    private var viewMoreRunnable: Runnable = Runnable {delegate?.viewMore(paginationData.currentPage, paginationData.pageSize)}
    private var total = 0
    private var isBusy = false
    protected var paginationData:InfinityPaginationData<T> = InfinityPaginationData(pageSize)

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        delegate = null
        viewMoreHandler.removeCallbacks(viewMoreRunnable)
    }

    fun setDatas(datas:Array<T>) {
        paginationData.reset()
        paginationData.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas:Array<T>) {
        val idx = paginationData.datas.size
        paginationData.addAll(datas)
        notifyItemRangeInserted(idx, datas.size)
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

    @CallSuper
    open fun viewMoreComplete(datas:Array<T>) {
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
        if(position == total-1 && isViewMore && paginationData.isPageable && !isBusy) {
            isBusy = true
            paginationData.next()
            viewMoreHandler.post(viewMoreRunnable)
        }
    }

    interface Delegate {
        fun viewMore(page:Int, size:Int){}
    }
}