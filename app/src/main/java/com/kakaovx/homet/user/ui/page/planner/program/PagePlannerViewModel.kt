package com.kakaovx.homet.user.ui.page.planner.program

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class PagePlannerViewModel(/*repo: Repository*/) : ViewModel() {

    val tag = "page_planner_view_model_log"

    private var _response: MutableLiveData<PageLiveData>? = null
    val response: LiveData<PageLiveData>? get() = _response

    fun onCreateView() {
        Log.d(tag, "onCreateView()")
        _response = MutableLiveData()
    }

    fun onDestroyView() {
        Log.d(tag, "onDestroyView()")
        _response = null
    }

    private fun handleComplete(data: String) {
        Log.i(tag, "handleComplete")

        val liveData = PageLiveData()
        liveData.cmd = AppConst.LIVE_DATA_CMD_LIST
        liveData.message = data
        _response?.value = liveData
    }

    private fun handleError(err: Throwable) {
        Log.i(tag, "handleError ($err)")
    }

    override fun onCleared() {
        Log.i(tag, "onCleared()")
        super.onCleared()
    }
}
