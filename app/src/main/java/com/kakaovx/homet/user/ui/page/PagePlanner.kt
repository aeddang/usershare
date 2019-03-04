package com.kakaovx.homet.user.ui.page

import android.content.Context
import android.view.View
import com.kakaovx.homet.user.util.Log

import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.network.model.ResponseList
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.MainActivity
import kotlinx.android.synthetic.main.page_home.*
import kotlinx.android.synthetic.main.page_network.*

class PagePlanner : RxPageFragment() {

    private val TAG = javaClass.simpleName

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_planner)
                setDisplayShowTitleEnabled(true)
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_network
    }

    override fun onCreated() {
        super.onCreated()
        Log.d(TAG, "onCreated()")

        context?.let{ initView(it) }

        button.clicks().subscribe(this::getAllUsers).apply { disposables.add(this) }
        hideProgress()
    }

    private fun handleComplete(data: ResponseList<ResultData>) {
        Log.i(TAG, "handleComplete ($data)")
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
        /*
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "apple"
        api.searchRepositories(params)
        showProgress()

        RxObservableConverter.forNetwork(api.searchRepositories(params))
        .subscribe(
            this::handleComplete,
            this::handleError
        ).apply { disposables.add(this) }
        */
    }
}