package com.kakaovx.homet.user.ui.splash

import com.kakaovx.homet.lib.page.PageActivity
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.ui.PageFactory
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log

class SplashActivity : PageActivity<PageID>() {

    val TAG = javaClass.simpleName

    override fun onCreated() {
        Log.d(TAG, "onCreated")
        PagePresenter.getInstance<PageID>().pageStart(PageID.SPLASH)
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed")
    }

    override fun getPageAreaId(): Int {
        Log.d(TAG, "getPageAreaId()")
        return R.id.container
    }

    override fun getPageExitMsg(): Int {
        Log.d(TAG, "getPageExitMsg()")
        return R.string.notice_app_exit
    }

    override fun getHomes(): Array<PageID> {
        Log.d(TAG, "getHomes()")
        return arrayOf(PageID.SPLASH)
    }

    override fun getPageByID(id: PageID): PageFragment {
        Log.d(TAG, "getPageByID()")
        return PageFactory.getInstance().getPageByID(id)
    }

    override fun getPopupByID(id: PageID): PageFragment {
        Log.d(TAG, "getPopupByID()")
        return PageFactory.getInstance().getPageByID(id)
    }

    override fun getLayoutResId(): Int {
        Log.d(TAG, "getLayoutResId()")
        return R.layout.activity_splash
    }
}
