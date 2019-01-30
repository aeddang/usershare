package com.kakaovx.homet.user.ui.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.AppFragmentAutoClearedDisposable
import com.kakaovx.homet.user.util.Log
import com.kakaovx.homet.user.util.plusAssign
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SplashFragment : Fragment() {

    val TAG = javaClass.simpleName

    private val viewDisposable = AppFragmentAutoClearedDisposable(this)

    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory
    lateinit var viewModel: SplashViewModel

    private val autoLogin = true

    companion object {
        fun newInstance() = SplashFragment()
    }

    private fun routeToMainPage() {
        Log.d(TAG, "routeToMainPage()")
        val i = Intent(AppConst.HOMET_MAIN_ACTIVITY)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        activity?.finish()
    }

    private fun showLoginForm() {
        Log.d(TAG, "showLoginForm()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView()")
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated()")
        routeToMainPage()

        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]

        lifecycle += viewDisposable
        viewDisposable += viewModel.startLogin(autoLogin)

        viewModel.autoLoginResponse.observe(this, Observer {
            when (it) {
                true -> viewDisposable += viewModel.startLoginProcess()
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

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
    }
}
