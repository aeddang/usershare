package com.kakaovx.homet.user.component.ui.skeleton.model.adapter

import android.os.Handler
import androidx.annotation.CallSuper
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kakaovx.homet.user.component.ui.skeleton.model.data.InfinityPaginationData
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem

abstract class BaseAdapter<T>(private val isViewMore:Boolean = false, pageSize:Int = -1) : RecyclerView.Adapter<BaseAdapter.ViewHolder>() {
    class ViewHolder(val item: ListItem) : RecyclerView.ViewHolder(item)

    val isEmpty: MutableLiveData<Boolean> = MutableLiveData()
    var delegate: BaseAdapter.Delegate? = null
    private var viewMoreHandler: Handler = Handler()
    private var viewMoreRunnable: Runnable = Runnable {delegate?.viewMore(paginationData.currentPage, paginationData.pageSize)}
    private var total = 0
    private var isBusy = false
    private var paginationData:InfinityPaginationData<T> = InfinityPaginationData(pageSize)

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        delegate = null
        viewMoreHandler.removeCallbacks(viewMoreRunnable)
    }

    fun setDataArray(data:Array<T>): RecyclerView.Adapter<BaseAdapter.ViewHolder>{
        paginationData.reset()
        paginationData.addAll(data)
        notifyDataSetChanged()
        return this
    }

    fun addDataArray(data:Array<T>) {
        val idx = paginationData.data.size
        paginationData.addAll(data)
        notifyItemRangeInserted(idx, data.size)
    }

    fun insertData(data:T, idx:Int = -1) {
        val position = if (idx == -1) paginationData.data.size else idx
        if(position == -1 || position >= total) return
        paginationData.data.add(position, data)
        notifyItemInserted(position)
    }

    fun updateData(data:T, idx:Int) {
        if(idx == -1 || idx >= total) return
        paginationData.data[idx] = data
        notifyItemChanged(idx)
    }

    fun removeData(data:T) {
        val position = paginationData.data.indexOf(data)
        removeData(position)
    }

    fun removeData(idx:Int) {
        if(idx == -1 || idx >= total) return
        paginationData.data.removeAt(idx)
        notifyItemRemoved(idx)
    }

    fun removeAllData() {
        paginationData.reset()
        notifyDataSetChanged()
    }

    @CallSuper
    open fun viewMoreComplete(dataArray:Array<T>) {
        addDataArray(dataArray)
        isBusy = false
    }

    override fun getItemCount():Int {
        isEmpty.value = (paginationData.data.size == 0)
        total = paginationData.data.size
        return total
    }

    @CallSuper
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item.setData(paginationData.data[position])
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