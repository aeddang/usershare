package com.kakaovx.homet.user.ui.page

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.module.ComplexListAdapter
import com.kakaovx.homet.user.component.ui.module.HorizontalLinearLayoutManager
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
        val recyclerView: RecyclerView = program_list.recyclerView
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setHasOptionsMenu(true)
            }
        }
        complexListAdapter = ComplexListAdapter()
        complexListAdapter?.let {
            recyclerView.apply {
                layoutManager = HorizontalLinearLayoutManager(context)
                adapter = it
            }
            it.isEmpty.observe(this, Observer { existData ->
                existData?.let {
                    if (existData) program_list_empty.visibility = View.VISIBLE
                    else program_list_empty.visibility = View.INVISIBLE
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.page_home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.action_finder -> {
                    Log.i(TAG, "action finder")
                }
                else -> {
                    Log.e(TAG, "no menu")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}