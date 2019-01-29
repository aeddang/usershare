package com.kakaovx.homet.user.ui.splash

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kakaovx.homet.user.component.network.api.RestfulApi

class SplashViewModelFactory(private val restApi: RestfulApi): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SplashViewModel(restApi) as T
    }
}