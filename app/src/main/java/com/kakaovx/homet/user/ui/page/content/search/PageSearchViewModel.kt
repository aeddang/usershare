package com.kakaovx.homet.user.ui.page.content.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PageSearchViewModel(repo: Repository) : ViewModel() {

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

    fun getFoo(): Disposable {
        return restApi.getWorkoutList()
                        .retry(RetryPolicy.none())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { res ->
                            handleComplete(res.message)
                        }
                        .subscribe()
    }

    private fun handleComplete(data: String) {
        Log.i(TAG, "handleComplete")

        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_LIST
        liveData.message = data
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
