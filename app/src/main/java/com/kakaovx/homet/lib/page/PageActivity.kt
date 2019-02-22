package com.kakaovx.homet.lib.page

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.kakaovx.homet.user.util.Log

abstract class PageActivity<T> : AppCompatActivity(), View<T>, PageFragment.Delegate, Page {

    private val TAG = javaClass.simpleName

    open lateinit var pagePresenter: PagePresenter<T>;  protected set
    open var currentPage: PageFragment? = null; protected set
    protected lateinit var pageArea:ViewGroup

    @IdRes
    abstract fun getPageAreaId(): Int
    @StringRes
    abstract fun getPageExitMsg(): Int
    abstract fun getHomes():Array<T>

    private var exitCount = 0
    protected val popups = ArrayList<PageFragment>()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model = PageModel<T>()
        model.homes = getHomes()
        pagePresenter = PagePresenter(this, model)
        setContentView(getLayoutResId())
        pageArea = findViewById(getPageAreaId())
        onCreated()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        currentPage = null
        pagePresenter.onDestroy()
        popups.clear()
        onDestroyed()
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

    fun getCurrentFragment(): PageFragment? {
        return supportFragmentManager.fragments.last() as PageFragment
    }

    fun getPageAreaSize():Pair<Float,Float> {
        return Pair(pageArea.width.toFloat(),pageArea.height.toFloat())
    }

    protected open fun resetBackPressedAction() {
        exitCount = 0
    }

    protected open fun onBackPressedAction(): Boolean {
        if(exitCount == 1) return false
        exitCount ++
        Toast.makeText(this,getPageExitMsg(),Toast.LENGTH_LONG).show()
        return true
    }
    override fun onBackPressed() {
        getCurrentFragment()?.let{ if(!it.isBackAble())return }
        if(!pagePresenter.onBack()) return
        if(onBackPressedAction()) return
        super.onBackPressed()
    }

    abstract fun getPageByID(id:T): PageFragment

    final override fun onPageStart(id:T) {
        resetBackPressedAction()
        val willChangePage = getPageByID(id)
        willChangePage.pageID = id
        willChangePage.delegate = this
        willChangePage.pageType = PageFragment.PageType.INIT
        Log.d(TAG, "onPageStart() = {$id}")
        supportFragmentManager.beginTransaction().add(getPageAreaId(), willChangePage, id.toString()).commitNow()
    }

    final override fun onPageChange(id:T, param:Map<String, Any>, isBack:Boolean) {
        resetBackPressedAction()
        val willChangePage = getPageByID(id)
        willChangePage.pageID = id
        willChangePage.delegate = this
        willChangePage.pageType = if(isBack) PageFragment.PageType.OUT else PageFragment.PageType.IN
        if( !param.isEmpty()) willChangePage.setParam(param)
        Log.d(TAG, "onPageChange() = {$id}")
        supportFragmentManager.beginTransaction().add(getPageAreaId(), willChangePage, id.toString()).commitNow()
    }

    override fun onCreateAnimation(v:PageFragment) {
        if(v.pageType == PageFragment.PageType.POPUP) return
        currentPage?.let {
            it.pageType = v.pageType
            it.onDestroyAnimation()
        }
        currentPage = v
    }

    abstract fun getPopupByID(id:T): PageFragment

    final override fun onOpenPopup(id:T, param:Map<String, Any>) {
        resetBackPressedAction()
        val popup = getPopupByID(id)
        popup.pageID = id
        popup.pageType = PageFragment.PageType.POPUP
        if( !param.isEmpty()) popup.setParam(param)
        Log.d(TAG, "onOpenPopup() = {$id}")
        popups.add(popup)
        supportFragmentManager.beginTransaction().add(getPageAreaId(), popup, id.toString()).commitNow()
    }

    final override fun onClosePopup(id:T) {
        val popup = supportFragmentManager.findFragmentByTag(id.toString()) as PageFragment
        popups.remove(popup)
        popup.onClosePopupAnimation()
    }
}