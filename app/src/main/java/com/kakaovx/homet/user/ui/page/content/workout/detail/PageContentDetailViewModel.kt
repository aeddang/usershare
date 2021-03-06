package com.kakaovx.homet.user.ui.page.content.workout.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.network.model.WorkoutData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PageContentDetailViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    private var _response: MutableLiveData<PageLiveData>? = null
    val response: LiveData<PageLiveData>? get() = _response

    private var _content: MutableLiveData<WorkoutData>? = null
    val content: LiveData<WorkoutData>? get() = _content

    fun onCreateView() {
        Log.d(TAG, "onCreateView()")
        _response = MutableLiveData()
        _content = MutableLiveData()
    }

    fun onDestroyView() {
        Log.d(TAG, "onDestroyView()")
        _response = null
        _content = null
    }

    fun getWorkoutContent(exercise_id: String): Disposable {
        return restApi.getFreeWorkoutContent(id = exercise_id)
                        .retry(RetryPolicy.none())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map { res ->
                            _content?.value = res.data
//                            _content.postValue(res.data)
                        }
                        .subscribe()
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