package com.kakaovx.homet.lib.page

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.view.ViewGroup
import dagger.android.support.DaggerAppCompatActivity

abstract class PageActivity<T> : DaggerAppCompatActivity(), PagePresenter.View<T>, PageFragment.Delegate, Page {
    open val pagePresenter = PagePresenter(this, PageModel())
    open var currentPage: PageFragment? = null
        protected set

    protected lateinit var pageArea:ViewGroup
    @IdRes abstract fun getPageAreaId(): Int

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
        onDestroied()
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

    abstract fun onBackPressedAction(): Boolean
    override fun onBackPressed() {
        getCurentFragment()?.let{ if(!it.isBackAble())return }
        if(!pagePresenter.onBack()) return
        if(onBackPressedAction()) return
        super.onBackPressed()
    }

    abstract fun <T> getPageByID(id:T): PageFragment
    final override fun onPageStart(id:T) {
        val willChangePage = getPageByID(id)
        willChangePage.pageID = id
        willChangePage.delegate = this
        willChangePage.pageType = PageFragment.PageType.INIT
        supportFragmentManager.beginTransaction().add(getPageAreaId(),willChangePage).commit()
    }
    final override fun onPageChange(id:T, param:Map<String, Any>, isBack:Boolean) {
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

    abstract fun <T> getPopupByID(id:T): PageFragment
    final override fun onOpenPopup(id:T, param:Map<String, Any>) {
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