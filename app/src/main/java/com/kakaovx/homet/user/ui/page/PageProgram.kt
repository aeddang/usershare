package com.kakaovx.homet.user.ui.page

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.module.WorkoutAdapter
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.viewModel.PageProgramViewModel
import com.kakaovx.homet.user.ui.viewModel.PageProgramViewModelFactory
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_program.*
import kotlinx.android.synthetic.main.ui_recycler.view.*
import javax.inject.Inject


class PageProgram : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: PageProgramViewModelFactory
    private lateinit var viewModel: PageProgramViewModel

    private var workoutAdapter: WorkoutAdapter? = null

    private fun initView(context: Context) {
        val recyclerView: RecyclerView = listComponent.recyclerView
        workoutAdapter = WorkoutAdapter()
        workoutAdapter?.let {
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
        disposables += viewModel.getProgram()
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_program
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated() start")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[PageProgramViewModel::class.java]
        viewModel.response.observe(this, Observer { liveData ->
            liveData?.let {
                val cmd = liveData.cmd
                when (cmd) {
                    AppConst.LIVE_DATA_CMD_NONE -> Log.e(TAG, "none command")
                    AppConst.LIVE_DATA_CMD_STRING -> {
                        val data = liveData.message
                        data?.let {
                            workoutAdapter?.setData(arrayOf(data)) ?: Log.e(TAG, "adapter is null")
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
        workoutAdapter = null
        super.onDestroyed()
    }
}