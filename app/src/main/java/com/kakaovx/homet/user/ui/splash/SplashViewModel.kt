package com.kakaovx.homet.user.ui.splash

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.network.model.ApiResponse
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(private val restApi: RestfulApi) : ViewModel() {

    val TAG = javaClass.simpleName

    val response: MutableLiveData<Boolean> = MutableLiveData()
    val autoLoginResponse: MutableLiveData<Boolean> = MutableLiveData()

    private fun handleComplete(data: ApiResponse) {
        Log.i(TAG, "handleComplete")
        response.value = true
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }

    fun startLoginProcess(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "apple"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleComplete,
                this::handleError
            )
    }

    fun startLoginForm() {
        response.value = false
    }

    fun startLogin(isAuto: Boolean = true): Disposable
        = Observable.just(isAuto)
            .delay(2000L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                autoLoginResponse.value = it
            }
            .subscribe()

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        super.onCleared()
    }
}
