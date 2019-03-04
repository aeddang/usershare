package com.kakaovx.homet.user.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.ContentModel
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

class PageProgramViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    val response: MutableLiveData<PageLiveData> = MutableLiveData()

    fun getProgram(): Disposable {
        return restApi.getProgramList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .subscribe( { res ->
                Observable.fromIterable(res.data)
                    .map { data ->
                        ContentModel(data.title, data.description, data.thumbnail_preview)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { model ->
                        val liveData = PageLiveData()
                        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_PROGRAM
                        liveData.contentModel = model
                        response.value = liveData
                    }, { handleError(it) })
            }, { handleError(it) })
    }

    private fun handleComplete(data: ArrayList<ResultData>) {
        Log.i(TAG, "handleComplete")

        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
        liveData.item = data
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
