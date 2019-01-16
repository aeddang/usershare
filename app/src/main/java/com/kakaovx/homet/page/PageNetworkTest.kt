package com.kakaovx.homet.page

import android.util.Log
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.R
import com.kakaovx.homet.component.network.RxObservableConverter
import com.kakaovx.homet.component.ui.ApiPageFragment
import java.util.*
import kotlinx.android.synthetic.main.page_network.*

class PageNetworkTest : ApiPageFragment()
{
    override fun getLayoutResId(): Int {
        return R.layout.page_network
    }

    override fun init() {
        button.clicks().subscribe(this::getAllUsers)
        hideProgress()
    }

    private fun handleComplete(data: Objects) {
        Log.i("handleComplete","complete")
        hideProgress()
    }

    private fun handleError(err: Throwable) {
        Log.i("handleError",err.toString())
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
        api.searchRepositories(params)
        showProgress()

        RxObservableConverter.forNetwork(api.searchRepositories(params))
        .subscribe(
            this::handleComplete,
            this::handleError
        ).apply { disposables.add(this) }
    }
}