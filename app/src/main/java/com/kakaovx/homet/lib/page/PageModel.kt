package com.kakaovx.homet.lib.page

import java.util.*
import kotlin.collections.ArrayList

class PageModel<T> : PagePresenter.Model<T> {
    private var currentHistoryStack:T? = null
    private var currentParamStack:Map<String, Any>? = null
    private val historys = Stack<T>()
    private val params = Stack<Map<String, Any>>()
    private val popups = ArrayList<T>()

    override fun onDestroy() {
        currentHistoryStack = null
        currentParamStack = null
    }

    override fun addHistory(id: T, param:Map<String, Any>, isHistory:Boolean) {
        if(isHistory) {
            currentHistoryStack?.let { historys.push(it) }
            currentParamStack?.let { params.push(it) }
        }
        currentHistoryStack = id
        currentParamStack = param
    }

    override fun getHistory():Pair<T?, Map<String, Any>?>? {
        if(historys.empty()) return null
        currentHistoryStack = null
        currentParamStack = null
        return Pair(historys.pop(), params.pop())
    }

    override fun clearAllHistory() {
        historys.clear()
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