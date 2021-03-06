package com.kakaovx.homet.user.component.account

import android.annotation.SuppressLint
import android.content.Intent
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.checkMainThread
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.component.preference.AccountPreference
import com.kakaovx.homet.user.component.sns.*
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.ui.page.etc.account.PopupLogin
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

@CheckResult
fun AccountManager.statusChanged(): Observable<AccountStatus> {
    return StatusChangedObservable(this)
}

class AccountManager(val preference:AccountPreference): SnsLoginManager.Delegate{
    private val snsManager = SnsLoginManager( preference )
    val TAG = javaClass.simpleName + "SNS"
    private var delegates = ArrayList<Delegate>()

    interface Delegate{
        fun statusChanged(status:AccountStatus){}
    }
    fun addStatusChangedListener(delegate:Delegate){ delegates.add(delegate) }
    fun removeStatusChangedListener(delegate:Delegate){ delegates.remove(delegate) }


    var isLogin:Boolean = false; private set
        get(){ return snsManager.isSignup }

    init {
        snsManager.setOnStatusChangedListener( this )
        snsManager.autoSingup()
    }

    fun destory(){
        snsManager.destory()
        delegates.clear()
    }

    fun login(pageID:PageID?, params:Map<String, Any>? = null, isPopup:Boolean = false ){
        snsManager.standbySignupAllSnsType()
        val param = HashMap<String, Any>()
        pageID?.let { param[PopupLogin.WILL_CHANGE_PAGE] = it }
        params?.let { param[PopupLogin.WILL_CHANGE_PARAM] = it }
        param[PopupLogin.WILL_CHANGE_TYPE] = isPopup
        PagePresenter.getInstance<PageID>().openPopup(PageID.POPUP_LOGIN, param)
    }

    fun logout(){
        snsManager.logout()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return snsManager.onActivityResult(requestCode, resultCode, data)
    }

    fun getProfile(): Observable<SnsProfile>?{
        if( !snsManager.isSignup ) return null
        return snsManager.getProfile()
    }

    override fun onLogout(){
        Log.d(TAG, "onLogou")
        delegates.forEach { it.statusChanged(AccountStatus.Logout ) }
    }
    override fun onLogin(token:String){
        Log.d(TAG, "onLogin $token")
        delegates.forEach { it.statusChanged(AccountStatus.Login) }
    }

    override fun onError(error:SnsError){
        val status = AccountStatus.Error
        status.value = error
        delegates.forEach { it.statusChanged( status ) }
    }
}


private class StatusChangedObservable( private val module: AccountManager) : Observable<AccountStatus>() {
    @SuppressLint("RestrictedApi")

    override fun subscribeActual(observer: Observer<in AccountStatus>) {
        if ( !checkMainThread(observer))  return
        val listener = Listener( module, observer)
        observer.onSubscribe(listener)
        module.addStatusChangedListener(listener)
    }
    private class Listener(
        private val module: AccountManager,
        private val observer: Observer<in AccountStatus>
    ) : MainThreadDisposable(), AccountManager.Delegate {

        override fun statusChanged(status:AccountStatus) {
            if (isDisposed) return
            observer.onNext(status)
        }
        override fun onDispose() {
            module.removeStatusChangedListener(this)
        }
    }
}