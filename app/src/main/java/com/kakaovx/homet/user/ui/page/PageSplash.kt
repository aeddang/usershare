package com.kakaovx.homet.user.ui.page

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.splash.SplashViewModel
import com.kakaovx.homet.user.ui.splash.SplashViewModelFactory
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class PageSplash : RxPageFragment() {

    val TAG = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory

    private lateinit var viewModel: SplashViewModel

    private val autoLogin = true

    private fun routeToMainPage() {
        Log.d(TAG, "routeToMainPage()")
        val i = Intent(AppConst.HOMET_ACTIVITY_MAIN)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        activity?.finish()
    }

    private fun showLoginForm() {
        Log.d(TAG, "showLoginForm()")
    }

    override fun getLayoutResId(): Int {
        Log.d(TAG, "getLayoutResId()")
        return R.layout.page_splash
    }

    override fun onSubscribe() {
        super.onSubscribe()
        Log.d(TAG, "onSubscribe()")
        disposables += viewModel.startLogin(autoLogin)
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated() start")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]
        viewModel.autoLoginResponse.observe(this, Observer {
            when (it) {
                true -> disposables += viewModel.startLoginProcess()
                false -> viewModel.startLoginForm()
            }
        })

        viewModel.response.observe(this, Observer {
            when (it) {
                true -> routeToMainPage()
                false -> showLoginForm()
            }
        })

        super.onCreated()
        Log.d(TAG, "onCreated() end")
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        super.onDestroyed()
    }
}
