package com.kakaovx.homet.user.ui.page.etc.login


import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.member.MemberManager
import com.kakaovx.homet.user.component.member.MemberStatus
import com.kakaovx.homet.user.component.member.statusChanged
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.PageID
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.popup_login.*
import javax.inject.Inject


class PopupLogin : RxPageFragment() {

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


    @Inject lateinit var memberManager: MemberManager


    override fun getLayoutResId(): Int { return R.layout.popup_login }
    override fun onCreatedView() {
        AndroidSupportInjection.inject(this)
        super.onCreatedView()
        btnFaceBook.setReadPermissions("email")
    }

    override fun onSubscribe(){
        memberManager.statusChanged().subscribe{ status ->
            when(  status ){
                MemberStatus.Logout ->{  }
                MemberStatus.Login ->{ movePage() }
                MemberStatus.Error ->{  }
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