package lib.page

import java.util.*
import kotlin.collections.ArrayList

class PageModel<T> : PagePresenter.Model<T>
{
    private var currentHistoryStack:T? = null
    private val historys = Stack<T>()
    private val popups = ArrayList<T>()

    override fun addHistory(id: T,isHistory:Boolean) {
        if(isHistory) currentHistoryStack?.let { historys.push(it) }
        currentHistoryStack = id
    }

    override fun getHistory():T? {
        currentHistoryStack = null
        if(historys.empty()) return null
        return historys.pop()
    }

    override fun clearAllHistory() {
        historys.clear()
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