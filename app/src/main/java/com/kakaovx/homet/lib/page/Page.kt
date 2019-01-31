package com.kakaovx.homet.lib.page

import android.support.annotation.LayoutRes

interface Page {
    @LayoutRes
    fun getLayoutResId(): Int
    fun onCreated()
    fun onAttached(){}
    fun onDetached(){}
    fun onDestroyed()
}

interface Presenter<T> {
    fun goHome()
    fun goBack()
    fun toggleNavigation()
    fun showNavigation()
    fun hideNavigation()
    fun closePopup(id:T): PagePresenter<T>
    fun openPopup(id:T): PagePresenter<T>
    fun openPopup(id:T,param:Map<String, Any>): PagePresenter<T>
    fun pageStart(id:T): PagePresenter<T>
    fun pageChange(id:T,isHistory:Boolean=true,isBack:Boolean = false): PagePresenter<T>
    fun pageChange(id:T,param:Map<String, Any>,isHistory:Boolean=true,isBack:Boolean = false): PagePresenter<T>
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
    fun getHome():T
    fun addHistory(id:T, param:Map<String, Any>, isHistory:Boolean)
    fun getHistory():Pair<T?, Map<String, Any>?>?
    fun clearAllHistory()
    fun removePopup(id:T)
    fun addPopup(id:T)
    fun getPopup():T?
    fun onDestroy()
}