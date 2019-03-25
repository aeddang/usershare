package com.kakaovx.homet.user.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.constant.AppConst
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

    private val autoLogin = true

    companion object {
        fun newInstance() = SplashFragment()
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle += viewDisposables
        Log.d(TAG, "onCreate()")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
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

        viewDisposables += viewModel.startLogin(autoLogin)

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
}
