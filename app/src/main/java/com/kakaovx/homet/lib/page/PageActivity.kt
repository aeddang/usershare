package com.kakaovx.homet.lib.page

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.Toast

abstract class PageActivity<T> : AppCompatActivity(), PagePresenter.View<T>, PageFragment.Delegate, Page {
    open val pagePresenter = PagePresenter(this, PageModel())
    open var currentPage: PageFragment? = null
        protected set
    protected lateinit var pageArea:ViewGroup

    @IdRes abstract fun getPageAreaId(): Int
    @StringRes abstract fun getPageExitMsg(): Int

    private var exitCount = 0

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResId())
        pageArea = findViewById(getPageAreaId())
        onCreated()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        currentPage = null
        pagePresenter.onDestroy()
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

    open fun getCurentFragment(): PageFragment? {
        return supportFragmentManager.fragments.last() as PageFragment
    }

    open fun getPageAreaSize():Pair<Float,Float> {
        return Pair(pageArea.width.toFloat(),pageArea.height.toFloat())
    }

    protected fun resetBackPressedAction() {
        exitCount = 0
    }

    protected fun onBackPressedAction(): Boolean {
        if(exitCount == 1) return false
        exitCount ++
        Toast.makeText(this,getPageExitMsg(),Toast.LENGTH_LONG).show()
        return true
    }
    override fun onBackPressed() {
        getCurentFragment()?.let{ if(!it.isBackAble())return }
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
        supportFragmentManager.beginTransaction().add(getPageAreaId(),willChangePage).commit()
    }
    final override fun onPageChange(id:T, param:Map<String, Any>, isBack:Boolean) {
        resetBackPressedAction()
        val willChangePage = getPageByID(id)
        willChangePage.pageID = id
        willChangePage.delegate = this
        willChangePage.pageType = if(isBack) PageFragment.PageType.OUT else PageFragment.PageType.IN
        if( !param.isEmpty()) willChangePage.setParam(param)
        supportFragmentManager.beginTransaction().add(getPageAreaId(),willChangePage).commit()
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
        supportFragmentManager.beginTransaction().add(getPageAreaId(),popup,id.toString()).commit()
    }

    final override fun onClosePopup(id:T) {
        val popup = supportFragmentManager.findFragmentByTag(id.toString()) as PageFragment
        popup.onClosePopupAnimation()
    }
}