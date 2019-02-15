package com.kakaovx.homet.user.ui.page

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.module.ComplexListAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.viewModel.PageHomeViewModel
import com.kakaovx.homet.user.ui.viewModel.PageHomeViewModelFactory
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_home.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*
import javax.inject.Inject


class PageHome : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: PageHomeViewModelFactory
    private lateinit var viewModel: PageHomeViewModel

    private var complexListAdapter: ComplexListAdapter? = null

    private fun initView(context: Context) {
        val recyclerView: RecyclerView = listComponent.recyclerView
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_home)
                setDisplayShowTitleEnabled(true)
            }
        }
        complexListAdapter = ComplexListAdapter()
        complexListAdapter?.let {
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
        disposables += viewModel.getHomeData()
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_home
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        complexListAdapter = null
        super.onDestroyed()
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated() start")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageHomeViewModel::class.java]
        viewModel.response.observe(this, Observer { liveData ->
            liveData?.let {
                val cmd = liveData.cmd
                when (cmd) {
                    AppConst.LIVE_DATA_CMD_NONE -> Log.e(TAG, "none command")
                    AppConst.LIVE_DATA_CMD_STRING -> {
                        liveData.message?.let {
                            Log.d(TAG, "get Data title = ${liveData.message}")
                        } ?: Log.e(TAG, "message is null")
                    }
                    AppConst.LIVE_DATA_CMD_ITEM -> {
                        liveData.item?.let {
                            val data = liveData.item
                            data?.let {
                                complexListAdapter?.setDataArray(data.toTypedArray()) ?: Log.e(TAG, "adapter is null")
                            } ?: Log.e(TAG, "data is null")
                        }
                    }
                    else -> Log.e(TAG, "wrong command")
                }
            } ?: Log.e(TAG, "liveData is null")
        })
        super.onCreated()

        context?.let{ initView(it) }
        initDisposables()
        Log.d(TAG, "onCreated() end")
    }
}