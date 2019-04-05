package com.kakaovx.homet.user.ui.page.etc.account


import android.Manifest
import android.widget.Toast
import androidx.core.view.isVisible
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.lib.page.PageRequestPermission
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.account.AccountManager
import com.kakaovx.homet.user.component.account.AccountStatus
import com.kakaovx.homet.user.component.account.statusChanged
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.component.ui.view.toast.VXToast
import com.kakaovx.homet.user.ui.PageID
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.popup_login.*
import javax.inject.Inject


class PopupLogin : RxPageFragment(), PageRequestPermission {

    companion object {
        const val WILL_CHANGE_PAGE = "willChangePage"
        const val WILL_CHANGE_PARAM= "willChangeParam"
        const val WILL_CHANGE_TYPE = "willChangeType"
    }

    private var willChangePage:PageID? = null
    private var willChangeParam:Map<String, Any>? = null
    private var willChangeIsPopup:Boolean = false
    override fun setParam(param: Map<String, Any>): PageFragment {
        willChangePage = param[WILL_CHANGE_PAGE] as? PageID
        willChangeIsPopup = param[WILL_CHANGE_TYPE] as Boolean
        willChangeParam = param[WILL_CHANGE_PARAM] as? Map<String, Any>
        return super.setParam(param)
    }

    @Inject lateinit var accountManager: AccountManager
    override fun getLayoutResId(): Int { return R.layout.popup_login }
    override fun onCreatedView() {
        AndroidSupportInjection.inject(this)
        super.onCreatedView()
        PagePresenter.getInstance<PageID>().requestPermission( arrayOf( Manifest.permission.READ_PHONE_STATE ), this )
        btnFaceBook.setReadPermissions("email")
        setBtnStatus()
    }

    override fun onRequestPermissionResult(resultAll:Boolean ,  permissions: List<Boolean>?){
        if(resultAll) return
        context?.let { VXToast.makeToast(it, R.string.error_need_permission, Toast.LENGTH_SHORT).show() }
        PagePresenter.getInstance<PageID>().goBack()
    }

    private fun setBtnStatus(){

        if(accountManager.isLogin){
            btnFaceBook.isVisible = false
            btnKakao.isVisible = false
            btnLogout.isVisible = true
        }else{
            btnLogout.isVisible = false
            btnFaceBook.isVisible = true
            btnKakao.isVisible = true
        }
    }


    override fun onSubscribe(){
        btnLogout.clicks().subscribe { accountManager.logout()
        }.apply { disposables.add(this) }

        accountManager.statusChanged().subscribe{ status ->
            setBtnStatus()
            when(  status ){
                AccountStatus.Logout ->{  }
                AccountStatus.Login ->{ movePage() }
                AccountStatus.Error ->{  }
            }
        }.apply { disposables.add(this) }
    }

    private fun movePage(){
        val pid = pageID as PageID
        PagePresenter.getInstance<PageID>().closePopup( pid )
        willChangePage ?: return
        if(willChangeIsPopup){
            PagePresenter.getInstance<PageID>().openPopup(willChangePage!!, willChangeParam)
        }else{
            PagePresenter.getInstance<PageID>().pageChange(willChangePage!!, willChangeParam)
        }
    }

}