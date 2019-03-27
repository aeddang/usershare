package com.kakaovx.homet.user.ui.page.content.trainer

import androidx.lifecycle.LiveData
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

class PageTrainerViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    private var _response: MutableLiveData<PageLiveData>? = null
    val response: LiveData<PageLiveData>? get() = _response

    fun onCreateView() {
        Log.d(TAG, "onCreateView()")
        _response = MutableLiveData()
    }

    fun onDestroyView() {
        Log.d(TAG, "onDestroyView()")
        _response = null
    }

    fun getTrainer(): Disposable {
        Log.d(TAG, "getTrainer()")
        return restApi.getTrainerList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .subscribe( { res ->
                Observable.fromIterable(res.data)
                    .map { data ->
                        ContentModel(data.provider_id, data.provider_name, data.description, data.thumbnail_provider_profile)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( { model ->
                        val liveData = PageLiveData()
                        liveData.cmd = AppConst.LIVE_DATA_CMD_LIST
                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_TRAINER
                        liveData.contentModel = model
                        _response?.value = liveData
                    }, { handleError(it) })
                }, { handleError(it) })
    }

    private fun handleComplete(data: ArrayList<ResultData>) {
        Log.i(TAG, "handleComplete")

        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_LIST
        liveData.item = data
        _response?.value = liveData
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }

    override fun onCleared() {
        Log.i(TAG, "onCleared()")
        super.onCleared()
    }
}
