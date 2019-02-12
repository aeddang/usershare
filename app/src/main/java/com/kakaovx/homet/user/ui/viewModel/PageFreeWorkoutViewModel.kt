package com.kakaovx.homet.user.ui.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.network.model.ApiResponse
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.model.data.PageLiveData
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PageFreeWorkoutViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    val response: MutableLiveData<PageLiveData> = MutableLiveData()

    fun getProgram(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "microsoft"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleComplete,
                this::handleError
            )
    }

    fun getWorkoutInProgram(programIndex: Int): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "microsoft"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleComplete,
                this::handleError
            )
    }

    fun getFreeWorkout(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "workout"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleComplete,
                this::handleError
            )
    }

    fun getTrainer(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "fitness trainer"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleComplete,
                this::handleError
            )
    }

    private fun handleComplete(data: ApiResponse) {
        Log.i(TAG, "handleComplete ($data)")
        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_STRING
        liveData.message = data.toString()
        response.value = liveData
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        super.onCleared()
    }
}
