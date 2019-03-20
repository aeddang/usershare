package com.kakaovx.homet.user.ui.page

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeIssueProgramModel
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.component.ui.module.HomeListAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.model.layoutUtil.RecyclerViewBottomDecoration
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.viewModel.PageHomeIssueProgramListViewModel
import com.kakaovx.homet.user.ui.viewModel.PageHomeIssueProgramListViewModelFactory
import com.kakaovx.homet.user.util.AppUtil
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_home_content_issue_list.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*
import javax.inject.Inject

class PageHomeIssueProgramList : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: PageHomeIssueProgramListViewModelFactory
    private lateinit var viewModel: PageHomeIssueProgramListViewModel

    private var issueProgramListAdapter: HomeListAdapter<HomeIssueProgramModel>? = null

    companion object {
        fun newInstance(key: String): PageHomeIssueProgramList {
            val fragment = PageHomeIssueProgramList()
            val bundle = Bundle()
            bundle.putString(AppConst.HOMET_VALUE_RECOMMEND, key)
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun initView(context: Context) {
        issue_program_list?.let{
            issueProgramListAdapter = HomeListAdapter(AppConst.HOMET_LIST_ITEM_HOME_ISSUE_PROGRAM)
            issueProgramListAdapter?.let {
                issue_program_list.recyclerView?.apply {
                    layoutManager = VerticalLinearLayoutManager(context)
                    addItemDecoration(RecyclerViewBottomDecoration(20))
                    adapter = it
                }
                it.isEmpty.observe(this, Observer { value ->
                    if (value) issue_program_list_empty.visibility = View.VISIBLE
                    else issue_program_list_empty.visibility = View.INVISIBLE
                })
            } ?: Log.e(TAG, "home list adapter null")
        } ?: Log.e(TAG, "program_list null")
    }

    private fun insertData(liveData: PageLiveData) {
        AppUtil.getLiveDataListItemType(TAG, liveData.listItemType)
        when (liveData.listItemType) {
            AppConst.HOMET_LIST_ITEM_HOME_ISSUE_PROGRAM -> {
                liveData.homeIssueProgramModel?.let {
                    issueProgramListAdapter?.addData(it) ?: Log.e(TAG, "adapter is null")
                } ?: Log.e(TAG, "liveData.homeIssueProgramModel is null")
            }
            else -> Log.e(TAG, "wrong list type")
        }
    }

    override fun onSubscribe() {
        super.onSubscribe()
        val key = arguments?.getString(AppConst.HOMET_VALUE_RECOMMEND)
        key?.let {
            if (AppConst.HOMET_VALUE_NOTITLE != it) {
                Log.i(TAG, "initView() tab title = [$it]")

                disposables += viewModel.getIssueProgramData(it)
            }
        } ?: Log.e(TAG, "key is null")
    }

    override fun getLayoutResId() = R.layout.page_home_content_issue_list

    override fun onCreated() {
        Log.d(TAG, "onCreated()")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageHomeIssueProgramListViewModel::class.java]

        viewModel.response.observe(this, Observer { liveData ->
            liveData?.let {
                val cmd = liveData.cmd
                when (cmd) {
                    AppConst.LIVE_DATA_CMD_NONE -> Log.e(TAG, "none command")
                    AppConst.LIVE_DATA_CMD_STRING -> {
                        liveData.message?.let {
                            val message = liveData.message
                            Log.d(TAG, "message = [$message]")
                        } ?: Log.e(TAG, "message is null")
                    }
                    AppConst.LIVE_DATA_CMD_LIST -> {
                        insertData(liveData)
                    }
                    else -> Log.e(TAG, "wrong command")
                }
            } ?: Log.e(TAG, "liveData is null")
        })

        context?.let{ initView(it) }
        super.onCreated()
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        issueProgramListAdapter = null
        super.onDestroyed()
    }
}