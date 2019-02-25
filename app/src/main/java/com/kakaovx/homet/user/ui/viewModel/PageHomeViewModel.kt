package com.kakaovx.homet.user.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.HomeFreeWorkoutModel
import com.kakaovx.homet.user.component.model.HomeProgramModel
import com.kakaovx.homet.user.component.model.HomeTrainerModel
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
            .subscribe( { data ->
                Observable.fromIterable(data.items)
                    .map { item ->
                        HomeProgramModel(item.name, item.description, item.ownerData?.avatarUrl)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { model ->
                        val liveData = PageLiveData()
                        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_HOME_PROGRAM
                        liveData.homeProgramModel = model
                        response.value = liveData
                    }, { handleError(it) })
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
                Observable.fromIterable(data.items)
                    .map { item ->
                        HomeFreeWorkoutModel(item.description, item.fullName, item.ownerData?.avatarUrl)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { model ->
                        val liveData = PageLiveData()
                        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT
                        liveData.homeFreeWorkoutModel = model
                        response.value = liveData
                    }, { handleError(it) })
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
                Observable.fromIterable(data.items)
                    .map { item ->
                        HomeTrainerModel(item.name, item.description, item.ownerData?.type, item.ownerData?.avatarUrl)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { model ->
                        val liveData = PageLiveData()
                        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_HOME_TRAINER
                        liveData.homeTrainerModel = model
                        response.value = liveData
                    }, { handleError(it) })
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
                Observable.fromIterable(data.items)
                    .subscribeOn(Schedulers.io())
                    .map { it.name }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { message ->
                        val liveData = PageLiveData()
                        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_HOME_HASH_TAG
                        liveData.message = message
                        response.value = liveData
                    }, { handleError(it) })
            }, { handleError(it) })
    }

    private fun handleComplete(type: Int, data: ArrayList<ResultData>) {
        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
        liveData.listItemType = type
        liveData.item = data
        response.value = liveData
    }

    private fun handleComplete(data: ArrayList<ResultData>) {
        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
        liveData.item = data
        response.value = liveData
    }

    private fun handleComplete(message: String?) {
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
