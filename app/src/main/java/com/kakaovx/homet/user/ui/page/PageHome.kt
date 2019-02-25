package com.kakaovx.homet.user.ui.page

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeFreeWorkoutModel
import com.kakaovx.homet.user.component.model.HomeProgramModel
import com.kakaovx.homet.user.component.model.HomeTrainerModel
import com.kakaovx.homet.user.component.ui.module.HomeListAdapter
import com.kakaovx.homet.user.component.ui.module.HomePagerAdapter
import com.kakaovx.homet.user.component.ui.module.HorizontalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.model.data.PageLiveData
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.constant.AppFeature
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
import kotlinx.android.synthetic.main.ui_viewpager.view.*
import javax.inject.Inject


class PageHome : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: PageHomeViewModelFactory
    private lateinit var viewModel: PageHomeViewModel

    private var programListAdapter: HomeListAdapter<HomeProgramModel>? = null
    private var freeWorkoutListAdapter: HomeListAdapter<HomeFreeWorkoutModel>? = null
    private var trainerListAdapter: HomeListAdapter<HomeTrainerModel>? = null

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setHasOptionsMenu(true)
            }
            recommend_viewpager?.let {
                val viewPager = recommend_viewpager.viewPager
                viewPager.adapter = HomePagerAdapter(context, myActivity.supportFragmentManager)
                hash_tag_tab_layout?.setupWithViewPager(viewPager)
            }
        }

        program_list?.let{
            programListAdapter = HomeListAdapter(AppConst.HOMET_LIST_ITEM_HOME_PROGRAM)
            programListAdapter?.let {
                program_list.recyclerView?.apply {
                    layoutManager = HorizontalLinearLayoutManager(context)
                    adapter = it
                }
                it.isEmpty.observe(this, Observer { existData ->
                    existData?.let {
                        if (existData) program_list_empty.visibility = View.VISIBLE
                        else program_list_empty.visibility = View.INVISIBLE
                    }
                })
            } ?: Log.e(TAG, "home list adapter null")
        } ?: Log.e(TAG, "program_list null")

        free_workout_list?.let{
            freeWorkoutListAdapter = HomeListAdapter(AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT)
            freeWorkoutListAdapter?.let {
                free_workout_list.recyclerView.apply {
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
            trainerListAdapter = HomeListAdapter(AppConst.HOMET_LIST_ITEM_HOME_TRAINER)
            trainerListAdapter?.let {
                trainer_list.recyclerView.apply {
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
    }

    private fun insertData(liveData: PageLiveData) {
        when (liveData.listItemType) {
            AppConst.HOMET_LIST_ITEM_HOME_PROGRAM -> {
                liveData.homeProgramModel?.let {
                    programListAdapter?.addData(it) ?: Log.e(TAG, "adapter is null")
                }
            }
            AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT -> {
                liveData.homeFreeWorkoutModel?.let {
                    freeWorkoutListAdapter?.addData(it) ?: Log.e(TAG, "adapter is null")
                }
            }
            AppConst.HOMET_LIST_ITEM_HOME_TRAINER -> {
                liveData.homeTrainerModel?.let {
                    trainerListAdapter?.addData(it) ?: Log.e(TAG, "adapter is null")
                }
            }
            AppConst.HOMET_LIST_ITEM_HOME_HASH_TAG -> {
                liveData.message?.let {
                    val message = it
                    hash_tag_tab_layout?.apply {
                        if (tabCount < AppFeature.APP_FEATURE_VIEWPAGER_COUNT) {
                            Log.d(TAG, "message = [$message]")
                            addTab(newTab().setText(message))
                        }
                    } ?: Log.e(TAG, "tag tab is null")
                }
            }
            else -> Log.e(TAG, "wrong list type")
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_home
    }

    override fun onSubscribe() {
        super.onSubscribe()
        disposables += viewModel.getProgramData()
        disposables += viewModel.getFreeWorkoutData()
        disposables += viewModel.getTrainerData()
        disposables += viewModel.getHashTagData()
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        programListAdapter = null
        freeWorkoutListAdapter = null
        trainerListAdapter = null
        super.onDestroyed()
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated()")
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
                            Log.d(TAG, "message = [$message]")
                        } ?: Log.e(TAG, "message is null")
                    }
                    AppConst.LIVE_DATA_CMD_ITEM -> {
                        insertData(liveData)
                    }
                    else -> Log.e(TAG, "wrong command")
                }
            } ?: Log.e(TAG, "liveData is null")
        })
        context?.let{ initView(it) }
        super.onCreated()
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