package com.kakaovx.homet.lib.page

import java.util.*
import kotlin.collections.ArrayList

class PageModel<T> : Model<T> {
    private var currentHistoryStack:T? = null
    private var currentParamStack:Map<String, Any>? = null
    private val histories = Stack<T>()
    private val params = Stack<Map<String, Any>>()
    private val popups = ArrayList<T>()
    internal lateinit var homes: Array<T>

    override fun onDestroy() {
        currentHistoryStack = null
        currentParamStack = null
    }

    override fun getHome():T {
        return homes[0]
    }

    override fun addHistory(id: T, param:Map<String, Any>, isHistory:Boolean) {
        if(isHistory) {
            currentHistoryStack?.let { histories.push(it) }
            currentParamStack?.let { params.push(it) }
        }
        if(homes.indexOf(id) != -1) clearAllHistory()
        currentHistoryStack = id
        currentParamStack = param
    }

    override fun getHistory():Pair<T?, Map<String, Any>?>? {
        if(histories.empty()) return null
        currentHistoryStack = null
        currentParamStack = null
        return Pair(histories.pop(), params.pop())
    }

    override fun clearAllHistory() {
        histories.clear()
        params.clear()
    }

    override fun removePopup(id:T) {
        popups.remove(id)
    }

    override fun addPopup(id:T) {
        popups.add(id)
    }

    override fun getPopup():T? {
        if(popups.isEmpty()) return null
        return popups.last()
    }

}