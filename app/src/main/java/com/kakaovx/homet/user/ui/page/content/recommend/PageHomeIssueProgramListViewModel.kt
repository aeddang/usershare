package com.kakaovx.homet.user.ui.page.content.recommend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.HomeIssueProgramModel
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
import java.util.*

class PageHomeIssueProgramListViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    val response: MutableLiveData<PageLiveData> = MutableLiveData()

    fun getIssueProgramData(key: String): Disposable {
        return restApi.getIssueProgramList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .subscribe( { res ->
                Observable.fromIterable(res.data)
                    .map { data ->
                        HomeIssueProgramModel(data.title, data.description, data.thumbnail_intro)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { model ->
                        val liveData = PageLiveData()
                        liveData.cmd = AppConst.LIVE_DATA_CMD_LIST
                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_HOME_ISSUE_PROGRAM
                        liveData.homeIssueProgramModel = model
                        response.value = liveData
                    }, { handleError(it) })
            }, { handleError(it) })
    }

    private fun handleComplete(type: Int, data: ArrayList<ResultData>) {
        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_LIST
        liveData.listItemType = type
        liveData.item = data
        response.value = liveData
    }

    private fun handleComplete(data: ArrayList<ResultData>) {
        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_LIST
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
