package com.kakaovx.homet.user.component.ui.skeleton.model.data

import java.util.ArrayList

data class PaginationData<T>(val initPage:Int = 0, val isLoop:Boolean = false) {

    var currentPage = initPage; private set
    var totalCount:Int = 0; private set
    var datas = ArrayList<T>()
        set (newDatas){
            currentPage = initPage
            field = newDatas
            totalCount = newDatas.size
        }

    fun prev(): Boolean {
        var changePage = currentPage - 1
        if(changePage < 0)
        {
            if(isLoop) changePage = totalCount-1 else return false
        }
        currentPage = changePage
        return true
    }

    fun next(): Boolean {
        var changePage = currentPage + 1
        if(changePage >= totalCount)
        {
            if(isLoop) changePage = 0 else return false
        }
        currentPage = changePage
        return true
    }
}