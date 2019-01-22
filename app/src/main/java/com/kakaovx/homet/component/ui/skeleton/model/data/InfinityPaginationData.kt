package com.kakaovx.homet.component.ui.skeleton.model.data
import java.util.*

data class InfinityPaginationData<T>(val pageSize:Int = 10) {

    var currentPage = 0; private set
    var isPageable = true; private set
    var datas = ArrayList<T>(); private set

    fun reset(){
        currentPage = 0
        isPageable = true
        datas = ArrayList()
    }

    fun addAll(addDatas:ArrayList<T>): Int {
        datas.addAll(addDatas)
        if(addDatas.size < pageSize) isPageable = false
        return datas.size
    }

    fun next(): Int {
        currentPage ++
        return currentPage
    }


}
