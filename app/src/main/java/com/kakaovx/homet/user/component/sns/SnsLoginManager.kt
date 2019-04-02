package com.kakaovx.homet.user.component.sns

import android.app.Activity
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.widget.Toast
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.preference.MemberPreference
import com.kakaovx.homet.user.component.ui.view.toast.VXToast
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable


class SnsLoginManager(val preference:MemberPreference){
    val TAG = javaClass.simpleName + "SNS"
    private var disposables: CompositeDisposable? = null
    private var finalAccessToken:String = ""

    private val snss = arrayListOf( Kakao(SnsType.Kakao), FaceBook(SnsType.FaceBook))
    private var currentSnsType:SnsType? = null
    private var currentProgress:SnsStatus? = null
    private var isProgress = false
    private var delegate:Delegate? = null
    interface Delegate{
        fun onLogout()
        fun onLogin(token:String)
        fun onError(error:SnsError)
    }
    fun setOnStatusChangedListener( _delegate: SnsLoginManager.Delegate? ){ delegate = _delegate }


    var isBusy:Boolean = false; private set
        get(){ return isProgress }

    var isSignup:Boolean = false; private set
        get(){ return finalAccessToken!="" }

    init {
        val loginData = preference.getLoginData()
        val snsType = loginData.first
        if( snsType != -1){
            currentSnsType = SnsType.values().find { it.idx == snsType }
            finalAccessToken = loginData.second
            Log.d(TAG, "restore snstype $currentSnsType")
            Log.d(TAG, "restore key $finalAccessToken")
        }
        snss.forEach {sns->
            sns.create()
            sns.statusChanged().subscribe{ status->
                Log.d(TAG, "status $status")
                Log.d(TAG, "sns ${sns.type}")
                Log.d(TAG, "currentSnsType $currentSnsType")

                if((currentSnsType == sns.type && isProgress)
                    || currentSnsType == SnsType.ALL)
                {
                    if(status.progress >= currentProgress!!.progress){
                        progressComplete(status, sns.type)
                    }else{
                        when( status ){
                            SnsStatus.Logout ->{ sns.login() }
                            SnsStatus.Login ->{ sns.signUp() }
                            SnsStatus.Signup ->{
                                currentSnsType = sns.type
                                progressComplete(status, sns.type) }
                        }
                    }
                }
            }.apply { disposables?.add(this) }

            sns.error().subscribe { error->
                isProgress = false
                PagePresenter.getInstance<PageID>().activity?.getCurrentActivity()?.let {
                    when( error ){
                        SnsError.Client ->{ VXToast.makeToast(it, R.string.error_client, Toast.LENGTH_SHORT) }
                        SnsError.Session ->{ VXToast.makeToast(it, R.string.error_session, Toast.LENGTH_SHORT) }
                        SnsError.Network ->{ VXToast.makeToast(it, R.string.error_network, Toast.LENGTH_SHORT) }
                        SnsError.Server ->{ VXToast.makeToast(it, R.string.error_server, Toast.LENGTH_SHORT) }
                    }
                }
                if(currentSnsType == sns.type) { delegate?.onError(error) }

            }.apply { disposables?.add(this) }
        }
    }

    fun destory(){
        snss.forEach{ it.destroy() }
        delegate = null
        disposables?.dispose()
        disposables = null
    }

    fun autoSingup(){
        currentSnsType?.let { signup(it) }
    }

    fun getProfile(): Observable<Profile>?{
        if( !isSignup ) return null
        currentSnsType?.let { return snss[it.idx].profileUpdated() }
        return null
    }

    fun standbySignupAllSnsType( ){
        currentSnsType = SnsType.ALL
        currentProgress = SnsStatus.Signup
        Log.d(TAG, "setSnsType $currentSnsType")
    }

    fun login( type:SnsType ){
        if( isProgress ) return
        isProgress = true
        currentSnsType = type
        currentProgress = SnsStatus.Login
        currentSnsType?.let {
            if( snss[it.idx].status == SnsStatus.Logout){
                snss[it.idx].login()
            }else{
                progressComplete(SnsStatus.Login)
            }
        }
    }

    fun logout(){
        if( isProgress ) return
        isProgress = true
        currentProgress = SnsStatus.Logout
        if( currentSnsType == null ){
            progressComplete(SnsStatus.Logout)
        } else{
            let { snss[currentSnsType!!.idx].logout() }
        }
    }

    fun signup( type:SnsType ){
        Log.d(TAG, "signup $ type")
        if(type == SnsType.ALL) return
        if( isProgress ) return
        isProgress = true
        currentSnsType = type
        currentProgress = SnsStatus.Signup
        currentSnsType?.let {
            val sns = snss[it.idx]
            when(  sns.status ){
                SnsStatus.Logout ->{ sns.login() }
                SnsStatus.Login ->{ sns.signUp() }
                SnsStatus.Signup ->{ progressComplete(SnsStatus.Signup) }
            }
        }
    }

    private fun progressComplete(status:SnsStatus, snsType:SnsType? = null){
        isProgress = false
        if(status == SnsStatus.Logout) currentSnsType = null
        if(status == SnsStatus.Signup && currentSnsType == SnsType.ALL) currentSnsType = snsType
        if ( currentSnsType == null ) {
            Log.d(TAG, "progressComplete logout")
            finalAccessToken = ""
            preference.setLoginData(-1, finalAccessToken)
            delegate?.onLogout()
        }else{
            if(currentSnsType == SnsType.ALL) return
            finalAccessToken = snss[currentSnsType!!.idx].getAccessTokenID() ?: ""
            preference.setLoginData(currentSnsType!!.idx, finalAccessToken)
            Log.d(TAG, "progressComplete $currentSnsType")
            Log.d(TAG, "progressComplete $finalAccessToken")
            delegate?.onLogin(finalAccessToken)
        }

    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        snss.forEach { it.onActivityResult( requestCode, resultCode, data ) }
        return false
    }





}