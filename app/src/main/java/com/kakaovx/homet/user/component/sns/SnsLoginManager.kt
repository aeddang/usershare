package com.kakaovx.homet.user.component.sns

import android.content.Intent
import android.widget.Toast
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.preference.AccountPreference
import com.kakaovx.homet.user.component.ui.view.toast.VXToast
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable


class SnsLoginManager(val preference:AccountPreference){
    val TAG = javaClass.simpleName
    private var disposables: CompositeDisposable? = null
    private var finalAccessToken:String = ""

    private val snss = arrayListOf( SnsKakao(SnsType.Kakao), SnsFaceBook(SnsType.FaceBook))
    private var currentSnsType:SnsType = SnsType.ALL
    private var currentProgress:SnsStatus? = null
    private var isProgress = false
    private var delegate:Delegate? = null
    interface Delegate{
        fun onLogout()
        fun onLogin(token:String)
        fun onError(error:SnsError)
    }
    fun setOnStatusChangedListener( _delegate: SnsLoginManager.Delegate? ){ delegate = _delegate }

    var isSignup:Boolean = false; private set
        get(){ return finalAccessToken!="" }

    init {
        val loginData = preference.getLoginData()
        val snsType = loginData.first
        if( snsType != -1){
            val ctype = SnsType.values().find { it.idx == snsType }
            ctype?.let { currentSnsType = it }
            finalAccessToken = loginData.second
            Log.d(TAG, "restore snstype $currentSnsType")
            Log.d(TAG, "restore key $finalAccessToken")
        }else{
            Log.d(TAG, "logout")
        }
        snss.forEach {sns->
            if (!AppFeature.APP_KAKAO_DEV_LOGIN) {
                sns.create()
                sns.statusChanged().subscribe{ status->
                    if((currentSnsType == sns.type && isProgress)
                        || currentSnsType == SnsType.ALL)
                    {
                        if(status.progress >= currentProgress!!.progress){
                            progressComplete(status, sns.type)
                            return@subscribe
                        }
                        when( status ){
                            SnsStatus.Logout ->{ sns.login() }
                            SnsStatus.Login ->{ sns.signUp() }
                            SnsStatus.Signup ->{
                                currentSnsType = sns.type
                                progressComplete(status, sns.type) }
                        }
                    }
                }.apply { disposables?.add(this) }

                sns.error().subscribe { error->
                    isProgress = false
                    Log.d(TAG, "error $error")
                    PagePresenter.getInstance<PageID>().activity?.getCurrentActivity()?.let {
                        when( error ){
                            SnsError.Client ->{ VXToast.makeToast(it, R.string.error_client, Toast.LENGTH_SHORT).show()}
                            SnsError.Session ->{ VXToast.makeToast(it, R.string.error_session, Toast.LENGTH_SHORT).show() }
                            SnsError.Network ->{ VXToast.makeToast(it, R.string.error_network, Toast.LENGTH_SHORT).show() }
                            SnsError.Server ->{ VXToast.makeToast(it, R.string.error_server, Toast.LENGTH_SHORT).show() }
                        }
                    }
                    if(currentSnsType == sns.type) { delegate?.onError(error) }

                }.apply { disposables?.add(this) }
            } else {
                return@forEach
            }
        }
    }

    fun destory(){
        snss.forEach{ it.destroy() }
        delegate = null
        disposables?.dispose()
        disposables = null
    }

    fun autoSingup(){
       signup(currentSnsType)
    }

    fun getProfile(): Observable<SnsProfile>?{
        if( !isSignup ) return null
        if( currentSnsType == SnsType.ALL ) return null
        return snss[currentSnsType.idx].profileUpdated()
    }

    fun standbySignupAllSnsType( ){
        currentSnsType = SnsType.ALL
        currentProgress = SnsStatus.Signup
    }

    fun login( type:SnsType ){
        if(type == SnsType.ALL) return
        isProgress = true
        currentSnsType = type
        currentProgress = SnsStatus.Login
        if( snss[currentSnsType.idx].status == SnsStatus.Logout){
            snss[currentSnsType.idx].login()
        }else{
            progressComplete(SnsStatus.Login)
        }
    }

    fun logout(){
        isProgress = false
        snss.forEach { it.logout() }
        progressComplete(SnsStatus.Logout)
    }

    fun signup( type:SnsType ){
        if(type == SnsType.ALL) return
        isProgress = true
        currentSnsType = type
        currentProgress = SnsStatus.Signup
        val sns = snss[currentSnsType.idx]
        when(  sns.status ){
            SnsStatus.Logout ->{ sns.login() }
            SnsStatus.Login ->{ sns.signUp() }
            SnsStatus.Signup ->{ progressComplete(SnsStatus.Signup) }
        }
    }

    private fun progressComplete(status:SnsStatus, snsType:SnsType? = null){
        isProgress = false
        if(status == SnsStatus.Logout) {
            currentSnsType = SnsType.ALL
            finalAccessToken = ""
            preference.setLoginData(-1, finalAccessToken)
            delegate?.onLogout()
            return
        }
        if(status == SnsStatus.Signup && currentSnsType == SnsType.ALL && snsType != null ) currentSnsType = snsType
        if(currentSnsType == SnsType.ALL) return
        finalAccessToken = snss[currentSnsType.idx].getAccessTokenID() ?: ""
        preference.setLoginData(currentSnsType.idx, finalAccessToken)
        Log.d(TAG, "progressComplete $currentSnsType")
        Log.d(TAG, "progressComplete $finalAccessToken")
        delegate?.onLogin(finalAccessToken)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        snss.forEach { it.onActivityResult( requestCode, resultCode, data ) }
        return false
    }

}