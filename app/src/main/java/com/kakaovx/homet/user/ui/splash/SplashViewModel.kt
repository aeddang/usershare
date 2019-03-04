package com.kakaovx.homet.user.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.network.model.ResponseList
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SplashViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    val response: MutableLiveData<Boolean> = MutableLiveData()
    val autoLoginResponse: MutableLiveData<Boolean> = MutableLiveData()

    private fun handleComplete(data: ResponseList<ResultData>) {
        Log.i(TAG, "handleComplete ($data)")
        response.value = true
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }

    fun startLoginProcess(): Disposable {
        // samples
        return Observable.just("start")
            .subscribe ({
                response.value = true
            }, { handleError(it) })
    }

    fun startLoginForm() {
        response.value = false
    }

    fun startLogin(isAuto: Boolean = true): Disposable
        = Observable.just(isAuto)
            .delay(2000L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map { autoLoginResponse.value = it }
            .subscribe()

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        super.onCleared()
    }
}
