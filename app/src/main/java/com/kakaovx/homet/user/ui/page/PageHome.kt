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
import com.kakaovx.homet.user.component.network.model.ResultData
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
import kotlinx.android.synthetic.main.page_home_content_free_workout.*
import kotlinx.android.synthetic.main.page_home_content_program.*
import kotlinx.android.synthetic.main.page_home_content_recommend.*
import kotlinx.android.synthetic.main.page_home_content_trainer.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*
import javax.inject.Inject


class PageHome : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: PageHomeViewModelFactory
    private lateinit var viewModel: PageHomeViewModel

    private var programListAdapter: ComplexListAdapter? = null
    private var freeWorkoutListAdapter: ComplexListAdapter? = null
    private var trainerListAdapter: ComplexListAdapter? = null
    private var recommendListAdapter: ComplexListAdapter? = null

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setHasOptionsMenu(true)
            }
        }

        program_list?.let{
            val recyclerView: RecyclerView = program_list.recyclerView
            programListAdapter = ComplexListAdapter(AppConst.HOMET_LIST_ITEM_HOME_PROGRAM)
            programListAdapter?.let {
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

        free_workout_list?.let{
            val recyclerView: RecyclerView = free_workout_list.recyclerView
            freeWorkoutListAdapter = ComplexListAdapter(AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT)
            freeWorkoutListAdapter?.let {
                recyclerView.apply {
                    layoutManager = HorizontalLinearLayoutManager(context)
                    adapter = it
                }
                it.isEmpty.observe(this, Observer { existData ->
                    existData?.let {
                        if (existData) free_workout_list_empty.visibility = View.VISIBLE
                        else free_workout_list_empty.visibility = View.INVISIBLE
                    }
                })
            }
        }

        trainer_list?.let{
            val recyclerView: RecyclerView = trainer_list.recyclerView
            trainerListAdapter = ComplexListAdapter(AppConst.HOMET_LIST_ITEM_HOME_TRAINER)
            trainerListAdapter?.let {
                recyclerView.apply {
                    layoutManager = HorizontalLinearLayoutManager(context)
                    adapter = it
                }
                it.isEmpty.observe(this, Observer { existData ->
                    existData?.let {
                        if (existData) trainer_list_empty.visibility = View.VISIBLE
                        else trainer_list_empty.visibility = View.INVISIBLE
                    }
                })
            }
        }

        recommend_tab_layout?.apply {
//            setupWithViewPager(recommend_viewpager.viewPager)
        }
    }

    private fun initDisposables() {
        disposables += viewModel.getProgramData()
        disposables += viewModel.getFreeWorkoutData()
        disposables += viewModel.getTrainerData()
        disposables += viewModel.getHashTagData()
    }

    private fun insertData(type: Int, dataArray: Array<ResultData>) {
        when (type) {
            AppConst.HOMET_LIST_ITEM_HOME_PROGRAM -> {
                programListAdapter?.setDataArray(dataArray) ?: Log.e(TAG, "adapter is null")
            }
            AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT -> {
                freeWorkoutListAdapter?.setDataArray(dataArray) ?: Log.e(TAG, "adapter is null")
            }
            AppConst.HOMET_LIST_ITEM_HOME_TRAINER -> {
                trainerListAdapter?.setDataArray(dataArray) ?: Log.e(TAG, "adapter is null")
            }
            AppConst.HOMET_LIST_ITEM_HOME_RECOMMEND -> {
                recommendListAdapter?.setDataArray(dataArray) ?: Log.e(TAG, "adapter is null")
            }
            else -> Log.e(TAG, "wrong list type")
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_home
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        programListAdapter = null
        freeWorkoutListAdapter = null
        trainerListAdapter = null
        recommendListAdapter = null
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
                            val message = liveData.message
                            recommend_tab_layout?.apply {
                                if (tabCount < 4) {
//                                    Log.d(TAG, "add Tab [$tabCount][$message]")
                                    addTab(newTab().setText(message))
                                }
                            }
                        } ?: Log.e(TAG, "message is null")
                    }
                    AppConst.LIVE_DATA_CMD_ITEM -> {
                        liveData.item?.let {
                            val data = liveData.item
                            data?.let {
                                insertData(liveData.listItemType, data.toTypedArray())
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