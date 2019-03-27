package com.kakaovx.homet.lib.page

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import java.util.*
import android.annotation.SuppressLint

abstract class PageActivity<T> : AppCompatActivity(), View<T>, Page, Activity {

    private val TAG = javaClass.simpleName
    open lateinit var pagePresenter: PagePresenter<T>;  protected set
    protected lateinit var pageArea:ViewGroup

    @IdRes abstract fun getPageAreaId(): Int
    @StringRes abstract fun getPageExitMsg(): Int
    abstract fun getHomes():Array<T>
    protected open fun getBackStacks():Array<T>? { return null }
    @AnimRes protected open fun getPageStart(): Int { return android.R.anim.fade_in }
    @AnimRes protected open fun getPageIn(isBack:Boolean): Int { return if(isBack) android.R.anim.fade_in else android.R.anim.slide_in_left}
    @AnimRes protected open fun getPageOut(isBack:Boolean): Int { return if(isBack) android.R.anim.fade_out else android.R.anim.slide_out_right}
    @AnimRes protected open fun getPopupIn(): Int { return android.R.anim.fade_in }
    @AnimRes protected open fun getPopupOut(): Int { return android.R.anim.fade_in }
    protected open fun getSharedChange():Any { return ChangeBounds() }

    protected open var exitCount = 0
    private var currentPage: T? = null
    private val historys = Stack< Pair< T, Map< String, Any >? >> ()
    private val popups = ArrayList<T>()

    @SuppressLint("UseSparseArrays")
    private var currentRequestPermissions = HashMap< Int , PageRequestPermission >()


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = PageModel<T>()
        model.homes = getHomes()
        model.backStacks = getBackStacks()
        pagePresenter = PagePresenter(this, model)
        pagePresenter.activity = this
        setContentView(getLayoutResId())
        pageArea = findViewById(getPageAreaId())
        onCreatedView()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        currentPage = null
        pagePresenter.onDestroy()
        popups.clear()
        historys.clear()
        onDestroyedView()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onDetached()
    }

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onAttached()
    }

    override fun hasPermissions( permissions: Array<out String> ): Pair< Boolean, List<Boolean>>? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return null
        val permissionResults = ArrayList< Boolean >()
        var resultAll = true
        for (permission in permissions) {
            val grant =  checkSelfPermission( permission ) == PackageManager.PERMISSION_GRANTED
            permissionResults.add ( grant )
            if( !grant ) resultAll = false
        }
        return Pair(resultAll, permissionResults )
    }

    override fun requestPermission( permissions: Array<out String>, requester:PageRequestPermission )
    {
        val grantResult = currentRequestPermissions.size
        currentRequestPermissions[ grantResult ] = requester
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            requestPermissionResult( grantResult, true )
            return
        }

        hasPermissions(permissions)?.let {

            if ( !it.first ) {

                requestPermissions( permissions, grantResult)
            } else {
                requestPermissionResult(grantResult, true )
            }
        }
    }

    private fun requestPermissionResult(requestCode: Int, resultAll:Boolean , permissions: List<Boolean>? = null )
    {
        currentRequestPermissions[ requestCode ]?.onRequestPermissionResult(resultAll, permissions)
        currentRequestPermissions.remove(requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        hasPermissions(permissions)?.let { requestPermissionResult(requestCode, it.first, it.second) }
    }

    override fun getCurrentPageFragment(): PageFragment? {
        if( currentPage == null ) return null
        val fragment = supportFragmentManager.findFragmentByTag(currentPage.toString())
        return fragment as? PageFragment
    }

    override fun getCurrentFragment(): PageFragment? {
        if( supportFragmentManager.fragments.isEmpty() ) return null
        return supportFragmentManager.fragments.last() as? PageFragment
    }

    override fun getPageAreaSize():Pair<Float,Float> {
        return Pair(pageArea.width.toFloat(),pageArea.height.toFloat())
    }

    @CallSuper
    override fun onClearPageHistory() {
        val len = supportFragmentManager.backStackEntryCount
        for (i in 1..len) supportFragmentManager.popBackStackImmediate()
    }

    @CallSuper
    override fun onCloseAllPopup() {
        popups.forEach { p -> this.onClosePopup(p)}
    }

    override fun onBack() { onBackPressed() }
    override fun onBackPressed() {
        if( !popups.isEmpty() ){
            val last = popups.last()
            val lastPopup = supportFragmentManager.findFragmentByTag(last.toString()) as? PageFragment
            lastPopup?.let { if(!it.isBackAble()) return }
            popups.remove(last)
            onClosePopup(last)
            return
        }
        val currentFragment = getCurrentFragment()
        currentFragment?.let{ if(!it.isBackAble()) return }
        currentPage?.let { if( pagePresenter.model.isHome(it) )  onExitAction() else onBackPressedAction() }
    }

    protected open fun resetBackPressedAction() { exitCount = 0 }
    protected open fun onExitAction() {
        if(exitCount == 1) { finish() }
        else {
            exitCount ++
            Toast.makeText(this,getPageExitMsg(),Toast.LENGTH_LONG).show()
        }
    }
    protected open fun onBackPressedAction() {
        if( historys.isEmpty()) onExitAction()
        val backPage = historys.pop()
        pageChange( backPage.first , backPage.second , false, null, null, true )
    }


    private fun getSharedTransitionName(sharedElement: android.view.View,  transitionName:String):String{
        val name = ViewCompat.getTransitionName(sharedElement)
        if(name == null) ViewCompat.setTransitionName(sharedElement, transitionName)
        return transitionName
    }

    private fun getWillChangePageFragment(id:T, param:Map<String, Any>?, isPopup:Boolean): PageFragment{
        val isBackStack = pagePresenter.model.isBackStack(id)
        if( isBackStack ) {
            val backStackFragment = supportFragmentManager.findFragmentByTag( id.toString() ) as? PageFragment
            backStackFragment?.let { return backStackFragment }
        }
        val newFragment = if( isPopup ) getPopupByID(id)  else getPageByID(id)
        newFragment.pageID = id
        param?.let { newFragment.setParam(it) }
        return newFragment
    }

    abstract fun getPageByID(id:T): PageFragment
    final override fun onPageStart(id:T) { pageChange(id, null, true) }
    final override fun onPageChange(id:T, param:Map<String, Any>?, sharedElement: android.view.View?,  transitionName:String?) {
        pageChange(id, param, false, sharedElement,  transitionName )
    }
    private fun pageChange(id:T, param:Map<String, Any>? , isStart:Boolean = false, sharedElement: android.view.View? = null, transitionName:String? = null, isBack:Boolean = false) {
        onCloseAllPopup()
        resetBackPressedAction()
        val willChangePage = getWillChangePageFragment(id, param, false)
        val transaction = supportFragmentManager.beginTransaction()
        if(isStart) {
            transaction.setCustomAnimations(getPageStart(),getPageOut(false))
        } else {
            if(sharedElement == null) {
                var isReverse = isBack
                var currentPos = 9999
                val pos = id as? PagePosition
                pos?.let { currentPos = it.position }
                currentPage?.let {
                    val prevPos = it as? PagePosition
                    prevPos?.let { pp -> isReverse = pp.position > currentPos }
                }
                transaction.setCustomAnimations(getPageIn(isReverse),getPageOut(isReverse))
            }else {
                transaction.setReorderingAllowed(true)
                transitionName?.let { transaction.addSharedElement(sharedElement, getSharedTransitionName(sharedElement,it)) }
                willChangePage.sharedElementEnterTransition = getSharedChange()
            }
        }
        currentPage?.let { if( pagePresenter.model.isBackStack(it) ) transaction.addToBackStack(it.toString()) }
        transaction.replace(getPageAreaId(), willChangePage, id.toString())
        transaction.commit()
        if( !isBack ) currentPage?.let { historys.push(Pair(it, param)) }
        currentPage = id

    }

    abstract fun getPopupByID(id:T): PageFragment
    final override fun onOpenPopup(id:T, param:Map<String, Any>?, sharedElement: android.view.View?, transitionName:String?) {
        resetBackPressedAction()
        val popup = getWillChangePageFragment(id, param, true)
        val transaction = supportFragmentManager.beginTransaction()
        if(sharedElement == null) {
            transaction.setCustomAnimations(getPopupIn(), getPopupOut())
        }else {
            transaction.setReorderingAllowed(true)
            transitionName?.let { transaction.addSharedElement(sharedElement, getSharedTransitionName(sharedElement,it)) }
            popup.sharedElementEnterTransition = getSharedChange()
            getCurrentPageFragment()?.let { transaction.hide(it) }
        }
        transaction.add(getPageAreaId(), popup, id.toString())
        transaction.commit()

        if(sharedElement != null) {
            getCurrentPageFragment()?.let { supportFragmentManager.beginTransaction(). show(it).commit() }
        }
        popups.add(id)
    }


    final override fun onClosePopup(id:T) {
        val fragment = supportFragmentManager.findFragmentByTag(id.toString())
        fragment?.let { closePopup(it) }
        popups.remove(id)
    }

    private fun closePopup(pop: Fragment, id:T? = null){
        val transaction = supportFragmentManager.beginTransaction()
        id?.let { if( pagePresenter.model.isBackStack(it) )  transaction.addToBackStack(it.toString()) }
        transaction.setCustomAnimations(getPopupIn(), getPopupOut())
            .remove(pop).commitNow()
    }
}