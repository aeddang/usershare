package com.kakaovx.homet.user.ui.page

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.network.RxObservableConverter
import com.kakaovx.homet.user.component.network.model.ApiResponse
import com.kakaovx.homet.user.component.ui.skeleton.injecter.ApiPageFragment
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.page_network.*

class PageNetworkTest : ApiPageFragment() {

    private val TAG = javaClass.simpleName

    override fun getLayoutResId(): Int {
        return R.layout.page_network
    }

    override fun onCreated() {
        super.onCreated()
        button.clicks().subscribe(this::getAllUsers).apply { disposables.add(this) }
        hideProgress()
    }

    private fun handleComplete(data: ApiResponse) {
        Log.i(TAG, "handleComplete"+ data.toString())
        hideProgress()
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
        hideProgress()
    }
    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
        progressBar.animate()
    }
    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    fun getAllUsers(v: Unit) {
        val params: MutableMap<String, String> = mutableMapOf()
        val restApi = api.restApi
        params["q"] = "apple"
        restApi.searchRepositories(params)
        showProgress()

        RxObservableConverter.forNetwork(restApi.searchRepositories(params))
        .subscribe(
            this::handleComplete,
            this::handleError
        ).apply { disposables.add(this) }
    }
}