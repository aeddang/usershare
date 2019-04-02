package com.kakaovx.homet.user.component.sns

import android.content.Intent
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.facebook.AccessTokenTracker
import com.facebook.Profile
import com.facebook.ProfileTracker
import com.kakaovx.homet.user.util.Log


class SnsFaceBook(type: SnsType): SnsModule(type) {

    val TAG = javaClass.simpleName
    var callbackManager: CallbackManager? = null; private set
    private var accessToken:AccessToken? = null
    private var accessTokenTracker:AccessTokenTracker? = null
    private var profileTracker:ProfileTracker? = null

    override fun create() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    checkStatus()
                }

                override fun onCancel() {
                    onStatusError( SnsError.Client )
                    onStatusChanged( SnsStatus.Logout )
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG, "onError ${exception.message}")
                    onStatusError( SnsError.Server )
                    onStatusChanged( SnsStatus.Logout )
                }
            })

        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged( oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
                accessToken = currentAccessToken
                accessTokenTracker?.stopTracking()
                onStatusChanged( SnsStatus.Signup )
            }
        }

        profileTracker = object : ProfileTracker() {
            override fun onCurrentProfileChanged( oldProfile: Profile?, currentProfile: Profile?) {
                profileTracker?.stopTracking()
                currentProfile?.let {
                    val profile = SnsProfile(it.id, it.name)
                    profile.imgPath = it.getProfilePictureUri(150, 150).path
                    onProfileUpdated(profile)
                    return
                }
                onProfileError(SnsError.Client)
            }
        }
        checkStatus()

    }

    private fun checkStatus(){
        accessToken = AccessToken.getCurrentAccessToken()
        if(accessToken == null) onStatusChanged( SnsStatus.Logout )
        accessToken?.let { token ->
            if (!token.isExpired) {
                onStatusChanged(SnsStatus.Signup)
            }else {
                onStatusChanged( SnsStatus.Login )
                accessTokenTracker?.let { if( !it.isTracking ) it.startTracking() }
            }
        }
    }

    override fun destroy(){
        super.destroy()
        callbackManager = null
        accessTokenTracker?.stopTracking()
        profileTracker?.stopTracking()
        accessTokenTracker = null
        profileTracker = null
    }

    override fun getAccessTokenID():String?{
        return accessToken?.token
    }

    override fun login(){
        //LoginManager.getInstance().logInWithReadPermissions(activity,  Arrays.asList("public_profile", "email"))
    }

    override fun signUp(){
        accessTokenTracker?.let { if( !it.isTracking ) it.startTracking() }
    }

    override fun logout(){
        LoginManager.getInstance().logOut()
        onStatusChanged( SnsStatus.Logout )
    }

    override fun requestProfile(){
        if(userProfile != null) {
            onProfileUpdated( userProfile!! )
            return
        }
        profileTracker?.let { if( !it.isTracking ) it.startTracking() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        return false
    }


}