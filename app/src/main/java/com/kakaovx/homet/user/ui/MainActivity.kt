package com.kakaovx.homet.user.ui

import android.content.Intent
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PageActivity
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.account.AccountManager
import com.kakaovx.homet.user.component.deeplink.DeepLinkManager
import com.kakaovx.homet.user.component.ui.skeleton.rx.Rx
import com.kakaovx.homet.user.component.ui.skeleton.view.DivisionTab
import com.kakaovx.homet.user.util.AppUtil
import com.kakaovx.homet.user.util.Log
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : PageActivity<PageID>(), DivisionTab.Delegate<PageID>, Rx {

    private val TAG = javaClass.simpleName
    private var disposables: CompositeDisposable = CompositeDisposable()
    @Inject
    lateinit var accountManager: AccountManager
    @Inject
    lateinit var deepLinkManager:DeepLinkManager

    override fun onCreatedView() {
        Log.d(TAG, "onCreatedView()")
        AndroidInjection.inject(this)
        PagePresenter.getInstance<PageID>().pageStart(PageID.CONTENT)
        bottomTab.delegate = this
        val keys = AppUtil.getApplicationSignature(this)
        Log.d(TAG, "onCreatedView() $keys")
        onSubscribe()
    }

    override fun onSubscribe() {
        btnEx.clicks().subscribe { PagePresenter.getInstance<PageID>().pageChange(PageID.TEST) }.apply { disposables.add(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        accountManager.destory()
        disposables.clear()
    }

    override fun onDestroyedView() {
        Log.d(TAG, "onDestroyedView()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if( accountManager.onActivityResult(requestCode, resultCode, data) )  return
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSelected(view:DivisionTab<PageID>, id:PageID) {
        Log.d(TAG, "onSelected() = {$id}")
        PagePresenter.getInstance<PageID>().pageChange(id)
    }

    override fun getPageByID(id:PageID): PageFragment {
        Log.d(TAG, "getPageByID() = {$id}")
        bottomTab.setSelect(id)
        return PageFactory.getInstance().getPageByID(id)
    }

    override fun getPopupByID(id:PageID): PageFragment {
        Log.d(TAG, "getPopupByID() = {$id}")
        return PageFactory.getInstance().getPageByID(id)
    }

    override  fun isChangePageAble(id:PageID, param:Map<String, Any>?, isPopup:Boolean):Boolean {
        val needLogin = PageFactory.getInstance().isNeedLoginPage( id )
        if(needLogin && !accountManager.isLogin){
            accountManager.login(id, param, isPopup)
            return false
        }
        return true
    }

    override fun onWillChangePageFragment(id:PageID, param:Map<String, Any>?, isPopup:Boolean){
        if( PageFactory.getInstance().isBottomTabHidden(id) ) bottomTab.hideTab() else bottomTab.viewTab()
    }

    override fun getLayoutResId(): Int { return R.layout.activity_main }
    override fun getPageAreaId(): Int { return R.id.area }
    override fun getPageExitMsg(): Int { return R.string.notice_app_exit }
    override fun getHomes():Array<PageID> { return PageFactory.getInstance().homePages }
    override fun getBackStacks():Array<PageID> { return PageFactory.getInstance().backStackPages }
    override fun getPageIn(isBack:Boolean): Int { return if(isBack) R.anim.slide_in_left else R.anim.slide_in_right }
    override fun getPageOut(isBack:Boolean): Int { return if(isBack) R.anim.slide_out_right else R.anim.slide_out_left }
    override fun getPopupIn(): Int { return R.anim.slide_in_down }
    override fun getPopupOut(): Int { return  R.anim.slide_out_down }
}

