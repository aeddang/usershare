package com.kakaovx.homet.user.component.ui.skeleton.model.data

import java.util.*

data class InfinityPaginationData<T>(val pageSize:Int = 10) {

    var currentPage = 0
    var isPageable = true
    var data = ArrayList<T>()

    fun reset() {
        currentPage = 0
        isPageable = true
        data = ArrayList()
    }

    fun addAll(addData:Array<T>): Int {
        data.addAll(addData)

        if(addData.size < pageSize) {
            isPageable = false
        }

        return data.size
    }

    fun next(): Int {
        currentPage ++
        return currentPage
    }
}
