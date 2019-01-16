package lib.page

import java.util.*
import kotlin.collections.ArrayList

class PageModel<T> : PagePresenter.Model<T>
{
    var currentHistoryStack:Any? = null
    val historys = Stack<Any>()
    val popups = ArrayList<Any>()

    override fun addHistory(id: T,isHistory:Boolean)
    {
        if(isHistory) currentHistoryStack?.let { historys.push(it) }
        currentHistoryStack = id
    }

    override fun getHistory():T?
    {
        currentHistoryStack = null
        if(historys.empty()) return null
        return historys.pop() as T?
    }

    override fun clearAllHistory() {
        historys.clear()
    }

    override fun removePopup(id:T)
    {
        popups.remove(id as Any)
    }

    override fun addPopup(id:T)
    {
        popups.add(id as Any)
    }
    override fun getPopup():T?
    {
        if(popups.isEmpty()) return null
        return popups.last() as T?
    }

}