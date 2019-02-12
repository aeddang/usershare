package com.kakaovx.homet.user.ui.page

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.module.ContentListAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.viewModel.PageFreeWorkoutViewModel
import com.kakaovx.homet.user.ui.viewModel.PageFreeWorkoutViewModelFactory
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_program.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*
import javax.inject.Inject


class PageFreeWorkout : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: PageFreeWorkoutViewModelFactory
    private lateinit var viewModel: PageFreeWorkoutViewModel

    private var contentListAdapter: ContentListAdapter? = null

    private fun initView(context: Context) {
        val recyclerView: RecyclerView = listComponent.recyclerView
        contentListAdapter = ContentListAdapter(AppConst.HOMET_LIST_ITEM_FREE_WORKOUT)
        contentListAdapter?.let {
            recyclerView.apply {
                layoutManager = VerticalLinearLayoutManager(context)
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
        disposables += viewModel.getFreeWorkout()
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_free_workout
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated() start")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageFreeWorkoutViewModel::class.java]
        viewModel.response.observe(this, Observer { liveData ->
            liveData?.let {
                val cmd = liveData.cmd
                when (cmd) {
                    AppConst.LIVE_DATA_CMD_NONE -> Log.e(TAG, "none command")
                    AppConst.LIVE_DATA_CMD_STRING -> {
                        val data = liveData.message
                        data?.let {
                            contentListAdapter?.setData(arrayOf(data)) ?: Log.e(TAG, "adapter is null")
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