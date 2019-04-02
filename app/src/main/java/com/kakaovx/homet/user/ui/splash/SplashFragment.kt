package com.kakaovx.homet.user.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakao.auth.ApiResponseCallback
import com.kakao.auth.AuthService
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.auth.network.response.AccessTokenInfoResponse
import com.kakao.network.ErrorResult
import com.kakao.util.exception.KakaoException
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.util.AppFragmentAutoClearedDisposable
import com.kakaovx.homet.user.util.Log
import com.kakaovx.homet.user.util.plusAssign
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SplashFragment : DaggerFragment() {

    val TAG = javaClass.simpleName

    private val viewDisposables = AppFragmentAutoClearedDisposable(this)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SplashViewModel

    private var sessionCallback: SessionCallback? = null

    private val autoLogin = true

    companion object {
        fun newInstance() = SplashFragment()
    }

    private fun routeToMainPage() {
        Log.d(TAG, "routeToMainPage()")
        val i = Intent(AppConst.HOMET_ACTIVITY_MAIN)
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        activity?.run {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    private fun showLoginForm() {
        Log.d(TAG, "showLoginForm()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        lifecycle += viewDisposables
        sessionCallback = SessionCallback()
        Session.getCurrentSession().addCallback(sessionCallback)
        Session.getCurrentSession().checkAndImplicitOpen()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
        Session.getCurrentSession().removeCallback(sessionCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated()")
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]

        if (!AppFeature.APP_KAKAO_DEV_LOGIN) {
            viewDisposables += viewModel.startLogin(autoLogin)
        }

        viewModel.autoLoginResponse.observe(this, Observer {
            when (it) {
                true -> viewDisposables += viewModel.startLoginProcess()
                false -> viewModel.startLoginForm()
            }
        })

        viewModel.response.observe(this, Observer {
            when (it) {
                true -> routeToMainPage()
                false -> showLoginForm()
            }
        })
    }

    private inner class SessionCallback : ISessionCallback {

        override fun onSessionOpened() {
            AuthService.getInstance().requestAccessTokenInfo(object : ApiResponseCallback<AccessTokenInfoResponse>() {
                override fun onSuccess(result: AccessTokenInfoResponse?) {
                    if (result != null) {
                        val userId = result.userId
                        viewModel.setUserId(userId)
                        Log.d(TAG, "onSuccess: userId = [$userId]")
                        routeToMainPage()
                    }
                }

                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.d(TAG, "onSessionClosed: " + errorResult.errorMessage)
                }

                override fun onNotSignedUp() {}
            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            exception?.apply {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                Log.e(TAG, "onSessionOpenFailed: ", exception)
            }
        }
    }
}
