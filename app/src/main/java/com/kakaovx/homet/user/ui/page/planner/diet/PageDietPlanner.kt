package com.kakaovx.homet.user.ui.page.planner.diet

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding3.view.clicks
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.network.model.ResponseList
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.ParamType
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_home.*
import kotlinx.android.synthetic.main.page_network.*
import javax.inject.Inject

class PageDietPlanner : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: ViewModelFactory
    private lateinit var plannerViewModel: PageDietPlannerViewModel

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

    @LayoutRes
    override fun getLayoutResId(): Int {
        return R.layout.page_network
    }

    override fun setParam(param: Map<String, Any>): PageFragment {
        Log.d(TAG, "setParam() data = [${param[ParamType.DETAIL.key]}]")
        return this
    }

    override fun onSubscribe() {
        disposables += plannerViewModel.getFoo()
    }

    override fun onCreatedView() {
        Log.d(TAG, "onCreatedView()")
        AndroidSupportInjection.inject(this)

        plannerViewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageDietPlannerViewModel::class.java]

        plannerViewModel.response.observe(this, Observer { message ->
            message?.let {
                Log.d(TAG, "message = [$message]")
            } ?: Log.e(TAG, "message is null")
        })

        context?.let{ initView(it) }
        super.onCreatedView()

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