package com.kakaovx.homet.lib.page

import android.view.View
import androidx.annotation.LayoutRes

interface PagePosition {
    var position: Int
}

interface Page {
    @LayoutRes
    fun getLayoutResId(): Int
    fun onCreated()
    fun onAttached(){}
    fun onDetached(){}
    fun onDestroyed()
}

interface PageRequestPermission {
    fun onRequestPermissionResult(resultAll:Boolean ,  permissions: List<Boolean>?){}
}

interface Presenter<T> {
    var activity:Activity?
    fun goHome(idx:Int = 0)
    fun goBack()
    fun toggleNavigation()
    fun showNavigation()
    fun hideNavigation()
    fun clearPageHistory(id:T): Presenter<T>
    fun closePopup(id:T): Presenter<T>
    fun closeAllPopup(id:T): Presenter<T>
    fun openPopup(id:T,param:Map<String, Any>? = null, sharedElement:View? = null, transitionName:String? = null): Presenter<T>
    fun pageStart(id:T): Presenter<T>
    fun pageChange(id:T,param:Map<String, Any>? = null, sharedElement:View? = null, transitionName:String? = null): Presenter<T>
    fun hasPermissions( permissions: Array<String> ): Boolean
    fun requestPermission( permissions: Array<out String>, requester:PageRequestPermission )
}

interface Activity {
    fun getCurrentPageFragment(): PageFragment?
    fun getCurrentFragment(): PageFragment?
    fun getPageAreaSize():Pair<Float,Float>
}

interface View<T> {
    fun onClearPageHistory()
    fun onPageStart(id:T)
    fun onBack()
    fun onPageChange(id:T,param:Map<String, Any>? = null, sharedElement: View? = null, transitionName:String? = null)
    fun onOpenPopup(id:T, param:Map<String, Any>? = null, sharedElement: View? = null, transitionName:String? = null)
    fun onClosePopup(id:T)
    fun onCloseAllPopup()
    fun onShowNavigation(){}
    fun onHideNavigation(){}
    fun hasPermissions( permissions: Array<out String> ):  Pair< Boolean, List<Boolean>>?
    fun requestPermission( permissions: Array<out String> , requester:PageRequestPermission)
}

interface Model<T> {
    fun getHome(idx:Int = 0):T
    fun isHome(id:T):Boolean
    fun isBackStack(id:T):Boolean
    fun onDestroy()
}