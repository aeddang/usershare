package com.kakaovx.homet.lib.page

class PagePresenter<T>(var view: View<T>?, internal val model: Model<T>): Presenter<T> {

    override var activity: Activity? = null
    companion object {
        internal const val TAG = "PagePresenter"
        private  var currentInstance:Any? = null
        @Suppress("UNCHECKED_CAST")
        fun <T> getInstance(): Presenter<T> {
            if(currentInstance == null) return PagePresenter(null, PageModel())
            return currentInstance as PagePresenter<T>
        }
    }

    fun onDestroy() {
        model.onDestroy()
        view = null
        activity = null
    }

    var isNavigationShow = false
        internal set (newValue) { field = newValue }

    init {
        currentInstance = this
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
    override fun goHome(idx:Int){
        pageChange(model.getHome())
    }

    override fun goBack(){
        view?.onBack()
    }

    override fun clearPageHistory(id:T): Presenter<T> {
        view?.onClearPageHistory()
        return this
    }

    override fun closeAllPopup(id:T): Presenter<T> {
        view?.onCloseAllPopup()
        return this
    }

    override fun closePopup(id:T): Presenter<T> {
        view?.onClosePopup(id)
        return this
    }


    override fun openPopup(id:T, param:Map<String, Any>?, sharedElement:android.view.View?, transitionName:String?): Presenter<T> {
        view?.onOpenPopup(id, param, sharedElement, transitionName)
        return this
    }

    override fun pageStart(id:T): Presenter<T> {
        view?.onPageStart(id)
        return this
    }

    override fun pageChange(id:T, param:Map<String, Any>?, sharedElement:android.view.View?, transitionName:String?): Presenter<T> {
        view?.onPageChange(id, param, sharedElement, transitionName)
        return this
    }

    override fun hasPermissions( permissions: Array<String> ): Boolean {
        view?.let { v ->
            v.hasPermissions( permissions )?.let { return it.first }
        }
        return false
    }
    override fun requestPermission( permissions: Array<out String>, requester:PageRequestPermission){
        view?.requestPermission(permissions, requester)
    }


}




