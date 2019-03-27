package com.kakaovx.homet.user.ui.page.content.recommend

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeFreeWorkoutModel
import com.kakaovx.homet.user.component.model.HomeProgramModel
import com.kakaovx.homet.user.component.model.HomeTrainerModel
import com.kakaovx.homet.user.component.ui.module.HomeListAdapter
import com.kakaovx.homet.user.component.ui.module.HomePagerAdapter
import com.kakaovx.homet.user.component.ui.module.HorizontalLinearLayoutManager
import com.kakaovx.homet.user.component.model.PageLiveData
import com.kakaovx.homet.user.component.ui.skeleton.model.layoutUtil.RecyclerViewRightDecoration
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.ui.ParamType
import com.kakaovx.homet.user.util.AppUtil
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_home.*
import kotlinx.android.synthetic.main.page_home_content_free_workout.*
import kotlinx.android.synthetic.main.page_home_content_program.*
import kotlinx.android.synthetic.main.page_home_content_issue.*
import kotlinx.android.synthetic.main.page_home_content_trainer.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*
import kotlinx.android.synthetic.main.ui_viewpager.view.*
import javax.inject.Inject


class PageHome : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: ViewModelFactory
    private lateinit var viewModel: PageHomeViewModel

    private var programListAdapter: HomeListAdapter<HomeProgramModel>? = null
    private var freeWorkoutListAdapter: HomeListAdapter<HomeFreeWorkoutModel>? = null
    private var trainerListAdapter: HomeListAdapter<HomeTrainerModel>? = null
    private var issuePagerAdapter: HomePagerAdapter? = null

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setHasOptionsMenu(true)
            }
            issue_program_viewpager?.apply {
                issuePagerAdapter = HomePagerAdapter(context, myActivity.supportFragmentManager)
            }
        }

        program_list?.let{
            programListAdapter = HomeListAdapter(AppConst.HOMET_LIST_ITEM_HOME_PROGRAM)
            programListAdapter?.let {
                program_list.recyclerView?.apply {
                    layoutManager = HorizontalLinearLayoutManager(context)
                    addItemDecoration(RecyclerViewRightDecoration(20))
                    adapter = it
                }
                it.isEmpty.observe(this, Observer { value ->
                    if (value) program_list_empty.visibility = View.VISIBLE
                    else program_list_empty.visibility = View.INVISIBLE
                })
            } ?: Log.e(TAG, "home list adapter null")
        } ?: Log.e(TAG, "program_list null")

        free_workout_list?.let{
            freeWorkoutListAdapter = HomeListAdapter(AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT)
            freeWorkoutListAdapter?.let {
                free_workout_list.recyclerView.apply {
                    layoutManager = HorizontalLinearLayoutManager(context)
                    addItemDecoration(RecyclerViewRightDecoration(20))
                    adapter = it
                }
                it.isEmpty.observe(this, Observer { value ->
                    if (value) free_workout_list_empty.visibility = View.VISIBLE
                    else free_workout_list_empty.visibility = View.INVISIBLE
                })
                it.itemPosition.observe(this, Observer { position ->
                    position?.apply {
                        val model = it.getData(this)
                        model.id?.apply {
                            val param = HashMap<String, Any>()
                            param[ParamType.DETAIL.key] = this
                            PagePresenter.getInstance<PageID>().pageChange(PageID.CONTENT_DETAIL, param = param)
                        }
                    }
                })
            }
        }

        trainer_list?.let{
            trainerListAdapter = HomeListAdapter(AppConst.HOMET_LIST_ITEM_HOME_TRAINER)
            trainerListAdapter?.let {
                trainer_list.recyclerView.apply {
                    layoutManager = HorizontalLinearLayoutManager(context)
                    addItemDecoration(RecyclerViewRightDecoration(20))
                    adapter = it
                }
                it.isEmpty.observe(this, Observer { value ->
                    if (value) trainer_list_empty.visibility = View.VISIBLE
                    else trainer_list_empty.visibility = View.INVISIBLE
                })
            }
        }
    }

    private fun insertData(liveData: PageLiveData) {
        AppUtil.getLiveDataListItemType(TAG, liveData.listItemType)
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
            AppConst.HOMET_LIST_ITEM_HOME_ISSUE_TAG -> {
                issue_program_viewpager?.apply {
                    val viewPager = viewPager
                    issuePagerAdapter?.apply {
                        liveData.homeIssueTags?.let { setDataList(it) } ?: Log.e(TAG, "homeIssueTags is null")
                        viewPager.adapter = issuePagerAdapter
                        hash_tag_tab_layout?.apply {
                            for (i in 0 until count) {
                                liveData.homeIssueTags?.let {
                                    addTab(newTab().setText(it[i]))
                                } ?: Log.e(TAG, "homeIssueTags is null")
                            }
                            setupWithViewPager(viewPager)
                        }
                    } ?: Log.e(TAG, "issuePagerAdapter is null")
                } ?: Log.e(TAG, "recommend_viewpager is null")
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
        disposables += viewModel.getIssueProgramData()
    }

    override fun onDestroyedView() {
        Log.d(TAG, "onDestroyedView()")
        programListAdapter = null
        freeWorkoutListAdapter = null
        trainerListAdapter = null
        issuePagerAdapter = null
        super.onDestroyedView()
    }

    override fun onCreatedView() {
        Log.d(TAG, "onCreatedView()")
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
                    AppConst.LIVE_DATA_CMD_LIST -> {
                        insertData(liveData)
                    }
                    else -> Log.e(TAG, "wrong command")
                }
            } ?: Log.e(TAG, "liveData is null")
        })
        context?.let{ initView(it) }
        super.onCreatedView()
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