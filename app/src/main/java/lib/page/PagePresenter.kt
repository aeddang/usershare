package lib.page

class PagePresenter<T>(val view: PagePresenter.View<T>,private val model: PagePresenter.Model<T>) {
    companion object {
        internal const val TAG = "Page"
        private var currentInstence:Any? = null
        fun <T> getInstence(): PagePresenter<T>? {
            return currentInstence as PagePresenter<T>
        }
    }

    init {
        currentInstence = this
    }

    fun showNavigation() {
        view.onShowNavigation()
    }

    fun hideNavigation() {
        view.onHideNavigation()
    }

    fun onBack():Boolean {
        val pop:T? = model.getPopup()
        pop?.let {
            closePopup(it)
            return false
        }
        val page:T? = model.getHistory()
        page?.let{
            pageChange(it,false,true)
            return false
        }
        return true
    }

    fun closePopup(id:T):PagePresenter<T> {
        model.removePopup(id)
        view.onClosePopup(id)
        return this
    }

    fun openPopup(id:T):PagePresenter<T> {
        view.onOpenPopup(id)
        model.addPopup(id)
        return this
    }

    fun pageChange(id:T,isHistory:Boolean=true,isBack:Boolean = false):PagePresenter<T> {
        view.onPageChange(id,isBack)
        model.addHistory(id,isHistory)
        return this
    }

    interface View<T> {
        fun onPageChange(id:T,isBack:Boolean)
        fun onOpenPopup(id:T)
        fun onClosePopup(id:T)
        fun onShowNavigation(){}
        fun onHideNavigation(){}
    }

    interface Model<T> {
        fun addHistory(id:T,isHistory:Boolean)
        fun getHistory():T?
        fun clearAllHistory()
        fun removePopup(id:T)
        fun addPopup(id:T)
        fun getPopup():T?
    }
}



