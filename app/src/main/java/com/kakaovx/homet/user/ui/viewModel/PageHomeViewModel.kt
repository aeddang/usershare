package com.kakaovx.homet.user.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.model.data.PageLiveData
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class PageHomeViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    val response: MutableLiveData<PageLiveData> = MutableLiveData()

    fun getProgramData(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "program"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { data ->
                handleComplete(AppConst.HOMET_LIST_ITEM_HOME_PROGRAM, data.items)
            }, { handleError(it) })
    }

    fun getFreeWorkoutData(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "free workout"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { data ->
                handleComplete(AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT, data.items)
            }, { handleError(it) })
    }

    fun getTrainerData(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "trainer"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { data ->
                handleComplete(AppConst.HOMET_LIST_ITEM_HOME_TRAINER, data.items)
            }, { handleError(it) })
    }

    fun getHashTagData(): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "hash tag"
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { data ->
//                Log.d(TAG, "get data = [$data]")
                Observable.fromIterable(data.items)
                    .map { it.name }
                    .subscribe( { message ->
//                        Log.d(TAG, "name = $message")
                        handleComplete(message)
                    }, { handleError(it) })
            }, { handleError(it) })
    }

    fun getRecommendData(key: String): Disposable {
        // samples
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = key
        return restApi.searchRepositories(params)
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { data ->
                handleComplete(AppConst.HOMET_LIST_ITEM_HOME_RECOMMEND, data.items)
            }, { handleError(it) })
    }

    private fun handleComplete(type: Int, data: ArrayList<ResultData>) {
//        Log.i(TAG, "handleComplete")

        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
        liveData.listItemType = type
        liveData.item = data
        response.value = liveData
    }

    private fun handleComplete(data: ArrayList<ResultData>) {
//        Log.i(TAG, "handleComplete")

        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
        liveData.item = data
        response.value = liveData
    }

    private fun handleComplete(message: String?) {
//        Log.i(TAG, "handleComplete")

        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_STRING
        liveData.message = message
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
