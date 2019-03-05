package com.kakaovx.homet.user.ui.page

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.ContentModel
import com.kakaovx.homet.user.component.ui.module.ContentListAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.model.layoutUtil.RecyclerViewBottomDecoration
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.viewModel.PageProgramViewModel
import com.kakaovx.homet.user.ui.viewModel.PageProgramViewModelFactory
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_program.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*
import javax.inject.Inject


class PageProgram : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: PageProgramViewModelFactory
    private lateinit var viewModel: PageProgramViewModel

    private var contentListAdapter: ContentListAdapter<ContentModel>? = null

    private fun initView(context: Context) {
        val recyclerView: RecyclerView = listComponent.recyclerView
        contentListAdapter = ContentListAdapter(AppConst.HOMET_LIST_ITEM_PROGRAM)
        contentListAdapter?.let {
            recyclerView.apply {
                layoutManager = VerticalLinearLayoutManager(context)
                addItemDecoration(RecyclerViewBottomDecoration(20))
                adapter = it
            }
            it.isEmpty.observe(this, Observer { existData ->
                existData?.let {
                    if (existData) viewEmpty.visibility = View.VISIBLE
                    else viewEmpty.visibility = View.INVISIBLE
                }
            })
        }
    }

    private fun initDisposables() {
        disposables += viewModel.getProgram()
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_program
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated() start")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageProgramViewModel::class.java]
        viewModel.response.observe(this, Observer { liveData ->
            liveData?.let {
                val cmd = liveData.cmd
                when (cmd) {
                    AppConst.LIVE_DATA_CMD_NONE -> Log.e(TAG, "none command")
                    AppConst.LIVE_DATA_CMD_STRING -> {
                        liveData.message?.let {
                            Log.d(TAG, "get Data title = ${liveData.message}")
                        }
                    }
                    AppConst.LIVE_DATA_CMD_ITEM -> {
                        liveData.contentModel?.let {
                            contentListAdapter?.addData(it) ?: Log.e(TAG, "adapter is null")
                        }
                    }
                    else -> Log.e(TAG, "wrong command")
                }
            }
        })
        super.onCreated()

        context?.let{ initView(it) }
        initDisposables()
        Log.d(TAG, "onCreated() end")
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        contentListAdapter = null
        super.onDestroyed()
    }
}