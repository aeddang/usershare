package com.kakaovx.homet.user.component.sns

import android.content.Intent
import com.kakao.auth.*
import com.kakao.util.exception.KakaoException

import com.kakaovx.homet.user.util.Log
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.ApiErrorCode
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.OptionalBoolean
import java.util.*


class Kakao(type: SnsType): SnsModule(type) {

    val TAG = javaClass.simpleName + "SNS"
    private var callback: SessionCallback? = null
    private var accessToken:String? = null
    override fun create(){
        try {
            KakaoSDK.init(KakaoSDKAdapter())
        } catch  (e:RuntimeException) {
            Log.d(TAG, "KakaoSDK already init")
        }
        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        if (Session.getCurrentSession().isOpenable) Session.getCurrentSession().checkAndImplicitOpen()
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            accessToken = Session.getCurrentSession().tokenInfo.accessToken
            onStatusChanged( SnsStatus.Login )

        }
        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Log.e(TAG, exception.errorType)
                var error = SnsError.Server
                when(exception.errorType){
                    KakaoException.ErrorType.AUTHORIZATION_FAILED ->{ error = SnsError.Client }
                    KakaoException.ErrorType.ILLEGAL_ARGUMENT ->{}
                    KakaoException.ErrorType.ILLEGAL_STATE ->{}
                    KakaoException.ErrorType.MISS_CONFIGURATION ->{}
                    KakaoException.ErrorType.CANCELED_OPERATION ->{ error = SnsError.Client }
                    KakaoException.ErrorType.UNSPECIFIED_ERROR ->{}
                    KakaoException.ErrorType.JSON_PARSING_ERROR ->{}
                    KakaoException.ErrorType.URI_LENGTH_EXCEEDED ->{}
                    KakaoException.ErrorType.KAKAOTALK_NOT_INSTALLED ->{ error = SnsError.Client }
                }
                onStatusError(error)
            }
            onStatusChanged( SnsStatus.Logout )
        }
    }

    override fun destroy(){
        super.destroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    override fun getAccessTokenID():String?{
        return accessToken
    }

    override fun login(){
       //Session.getCurrentSession().checkAndImplicitOpen()
    }

    override fun signUp(){
        requestSignUp()
    }

    override fun logout(){
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                onStatusChanged( SnsStatus.Logout )
            }
        })
    }

    override fun requestProfile(){
        if(userProfile != null) {
            onProfileUpdated( userProfile!! )
            return
        }
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                val message = "failed to get user info. msg=" + errorResult!!
                Log.d(TAG, "message $message")
                val result = errorResult.errorCode
                if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                    onProfileError(SnsError.Network)
                } else {
                    onProfileError(SnsError.Server)
                    onStatusChanged( SnsStatus.Logout )
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                onProfileError(SnsError.Session)
                onStatusChanged( SnsStatus.Logout )
            }

            override fun onSuccess(result: MeV2Response) = if (result.hasSignedUp() == OptionalBoolean.FALSE) {
                onProfileError(SnsError.Session)
                onStatusChanged( SnsStatus.Login )
            } else {
                val profile = Profile(result.kakaoAccount.displayId, result.nickname)
                profile.age = result.kakaoAccount.ageRange.ordinal
                profile.imgPath = result.profileImagePath
                profile.email = result.kakaoAccount.email
                profile.birthYear = result.kakaoAccount.birthyear
                profile.birth = result.kakaoAccount.birthday
                profile.phoneNumber = result.kakaoAccount.phoneNumber
                result.kakaoAccount.gender?.let {  profile.gender = it.value }
                onProfileUpdated( profile )
            }
        })
    }

    private fun requestSignUp(properties: Map<String, String> = HashMap()) {
        UserManagement.getInstance().requestSignup(object : ApiResponseCallback<Long>() {
            override fun onNotSignedUp() {
                onStatusChanged( SnsStatus.Login )
            }
            override fun onSuccess(result: Long?) {
                onStatusChanged( SnsStatus.Signup )
            }
            override fun onFailure(errorResult: ErrorResult?) {
                if(errorResult?.errorCode == -102){
                    onStatusChanged( SnsStatus.Signup )
                    return
                }
                val message = "UsermgmtResponseCallback : failure : " + errorResult!!
                Log.d(TAG, "Signup onFailure $message")
                onStatusError(SnsError.Server)
                onStatusChanged( SnsStatus.Login )
            }
            override fun onSessionClosed(errorResult: ErrorResult) {
                onStatusError(SnsError.Session)
                onStatusChanged( SnsStatus.Logout )
            }
        }, properties)
    }

    /*
    private fun requestAccessTokenInfo() {
        AuthService.getInstance().requestAccessTokenInfo(object : ApiResponseCallback<AccessTokenInfoResponse>() {
            override fun onSessionClosed(errorResult: ErrorResult) {
                onStatusError(SnsError.Session)
                onStatusChanged( SnsStatus.Logout )
            }

            override fun onNotSignedUp() {
                // not happened
            }

            override fun onFailure(errorResult: ErrorResult?) {
                val message = "failed to get access token info. msg=" + errorResult!!
                Log.e(TAG, message)
                onStatusError(SnsError.Server)
                onStatusChanged( SnsStatus.Login )
            }

            override fun onSuccess(accessTokenInfoResponse: AccessTokenInfoResponse) {
                val userId = accessTokenInfoResponse.userId
                val expiresInMilis = accessTokenInfoResponse.expiresInMillis
                Log.d(TAG, "expiresInMilis $expiresInMilis")
                //Session.getCurrentSession().checkAndImplicitOpen()
            }
        })
    }
    */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)
    }

}