package com.kakaovx.homet.lib.page

class PagePresenter<T>(var view: View<T>?, internal val model: Model<T>): Presenter<T> {

    companion object {
        internal const val TAG = "Page"
        private  var currentInstence:Any? = null
        @Suppress("UNCHECKED_CAST")
        fun <T> getInstence(): Presenter<T> {
            return currentInstence as PagePresenter<T>
        }
    }

    fun onDestroy() {
        model.onDestroy()
        currentInstence = null
        view = null
    }

    var isNavigationShow = false
        internal set (newValue) { field = newValue }

    init {
        currentInstence = this
    }

    override fun toggleNavigation() {
        if( isNavigationShow ) hideNavigation() else showNavigation()
    }

    override fun showNavigation() {
        isNavigationShow = true
        view?.onShowNavigation()
    }

    override fun hideNavigation() {
        isNavigationShow = false
        view?.onHideNavigation()
    }
    override fun goHome(){
        pageChange(model.getHome(),true, false)
    }

    override fun goBack(){ onBack() }
    internal fun onBack():Boolean {
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
            pageChange(it,tuple.second!!,false)
            return false
        }
        return true
    }

    override fun closePopup(id:T): PagePresenter<T> {
        model.removePopup(id)
        view?.onClosePopup(id)
        return this
    }

    override fun openPopup(id:T): PagePresenter<T> {
        return openPopup(id,HashMap())
    }

    override fun openPopup(id:T,param:Map<String, Any>): PagePresenter<T> {
        view?.onOpenPopup(id,param)
        model.addPopup(id)
        return this
    }

    override fun pageStart(id:T): PagePresenter<T> {
        view?.onPageStart(id)
        model.addHistory(id,HashMap(),true)
        return this
    }

    override fun pageChange(id:T,isHistory:Boolean,isBack:Boolean): PagePresenter<T> {
        return pageChange(id, HashMap(), isHistory, isBack)
    }
    override fun pageChange(id:T,param:Map<String, Any>,isHistory:Boolean,isBack:Boolean): PagePresenter<T> {
        view?.onPageChange(id,param,isBack)
        model.addHistory(id,param,isHistory)
        return this
    }


}




