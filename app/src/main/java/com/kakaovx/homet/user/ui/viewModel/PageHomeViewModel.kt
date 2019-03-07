package com.kakaovx.homet.user.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.HomeFreeWorkoutModel
import com.kakaovx.homet.user.component.model.HomeProgramModel
import com.kakaovx.homet.user.component.model.HomeTrainerModel
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PageHomeViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    val response: MutableLiveData<PageLiveData> = MutableLiveData()

    fun getProgramData(): Disposable {
        Log.d(TAG, "getProgramData()")
        return restApi.getRecommendProgramList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .subscribe( { res ->
                Observable.fromIterable(res.data)
                    .map { data ->
                        HomeProgramModel(data.title, data.description, data.thumbnail_preview)
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
        Log.d(TAG, "getFreeWorkoutData()")
        return restApi.getFreeWorkoutList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .subscribe( { res ->
                Observable.fromIterable(res.data)
                    .map { data ->
                        HomeFreeWorkoutModel(data.exercise_id, data.description, data.title, data.thumb_url)
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
        Log.d(TAG, "getTrainerData()")
        return restApi.getTrainerList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .subscribe( { res ->
                Observable.fromIterable(res.data)
                    .map { data ->
                        HomeTrainerModel(data.provider_name, data.description, data.trainer_alias, data.thumbnail_provider_profile)
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

    fun getIssueProgramData(): Disposable {
        Log.d(TAG, "getIssueProgramData()")
        val tagList: ArrayList<String> = ArrayList()
        return restApi.getIssueProgramList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .map { res ->
                Observable.fromIterable(res.data)
                    .map { data ->
                        data.program_id?.let{
                            tagList.add(it)
                        }
                    }
                    .subscribe()
                tagList
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { list ->
                val liveData = PageLiveData()
                liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
                liveData.listItemType = AppConst.HOMET_LIST_ITEM_HOME_ISSUE_TAG
                liveData.homeIssueTags = list
                response.value = liveData
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
