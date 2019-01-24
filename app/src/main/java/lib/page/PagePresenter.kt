package lib.page

class PagePresenter<T>(val view: PagePresenter.View<T>,private val model: PagePresenter.Model<T>) {


    companion object {
        internal const val TAG = "Page"
        private  lateinit var currentInstence:Any
        fun <T> getInstence(): PagePresenter<T> {
            return currentInstence as PagePresenter<T>
        }
    }
    var isNavigationShow = false
        internal set (newValue) { field = newValue }

    init {
        currentInstence = this
    }

    fun toggleNavigation() {
        if( isNavigationShow ) hideNavigation() else showNavigation()
    }

    fun showNavigation() {
        isNavigationShow = true
        view.onShowNavigation()
    }

    fun hideNavigation() {
        isNavigationShow = false
        view.onHideNavigation()
    }



    fun onBack():Boolean {
        if(isNavigationShow) {
            hideNavigation()
            return false
        }
        val pop:T? = model.getPopup()
        pop?.let {
            closePopup(it)
            return false
        }
        val tuple = model.getHistory()
        val page:T? = tuple?.first
        page?.let{
            pageChange(it,tuple.second!!,false,true)
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
        return openPopup(id,HashMap())
    }
    fun openPopup(id:T,param:Map<String, Any>):PagePresenter<T> {
        view.onOpenPopup(id,param)
        model.addPopup(id)
        return this
    }

    fun pageStart(id:T):PagePresenter<T> {
        view.onPageStart(id)
        model.addHistory(id,HashMap(),true)
        return this
    }

    fun pageChange(id:T,isHistory:Boolean=true,isBack:Boolean = false):PagePresenter<T> {
        return pageChange(id, HashMap(), isHistory, isBack)
    }
    fun pageChange(id:T,param:Map<String, Any>,isHistory:Boolean=true,isBack:Boolean = false):PagePresenter<T> {
        view.onPageChange(id,param,isBack)
        model.addHistory(id,param,isHistory)
        return this
    }

    interface View<T> {
        fun onPageStart(id:T)
        fun onPageChange(id:T,param:Map<String, Any>, isBack:Boolean)
        fun onOpenPopup(id:T, param:Map<String, Any>)
        fun onClosePopup(id:T)
        fun onShowNavigation(){}
        fun onHideNavigation(){}
    }

    interface Model<T> {
        fun addHistory(id:T, param:Map<String, Any>, isHistory:Boolean)
        fun getHistory():Pair<T?, Map<String, Any>?>?
        fun clearAllHistory()
        fun removePopup(id:T)
        fun addPopup(id:T)
        fun getPopup():T?
    }
}



