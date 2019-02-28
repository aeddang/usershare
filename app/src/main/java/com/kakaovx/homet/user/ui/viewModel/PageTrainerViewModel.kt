package com.kakaovx.homet.user.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.network.RetryPolicy
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.model.data.PageLiveData
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PageTrainerViewModel(repo: Repository) : ViewModel() {

    val TAG = javaClass.simpleName

    private val restApi = repo.restApi

    val response: MutableLiveData<PageLiveData> = MutableLiveData()

    fun getTrainer(): Disposable {
        return restApi.getTrainerList()
            .retry(RetryPolicy.none())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { data ->
                Log.i(TAG, "subscribeComplete")
                Log.i(TAG, "apiResponse code = ${data.code}")
                Log.i(TAG, "apiResponse message = ${data.message}")
                Log.i(TAG, "apiResponse raw = $data")
//                handleComplete(data.items)
            }, { handleError(it) })

//        return restApi.searchRepositories(params)
//            .retry(RetryPolicy.none())
//            .subscribeOn(Schedulers.io())
//            .subscribe( { data ->
//                Observable.fromIterable(data.items)
//                    .map { item ->
//                        HomeTrainerModel(item.name, item.description, item.ownerData?.type, item.ownerData?.avatarUrl)
//                    }
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe( { model ->
//                        val liveData = PageLiveData()
//                        liveData.cmd = AppConst.LIVE_DATA_CMD_ITEM
//                        liveData.listItemType = AppConst.HOMET_LIST_ITEM_HOME_TRAINER
//                        liveData.homeTrainerModel = model
//                        response.value = liveData
//                    }, { handleError(it) })
//            }, { handleError(it) })
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
