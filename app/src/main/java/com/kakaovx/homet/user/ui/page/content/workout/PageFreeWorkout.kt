package com.kakaovx.homet.user.ui.page.content.workout

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.kakaovx.homet.lib.page.PagePresenter
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.ContentModel
import com.kakaovx.homet.user.component.ui.module.ContentListAdapter
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.skeleton.model.layoutUtil.RecyclerViewBottomDecoration
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.ui.PageID
import com.kakaovx.homet.user.ui.ParamType
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_program.*
import kotlinx.android.synthetic.main.ui_recyclerview.view.*
import javax.inject.Inject


class PageFreeWorkout : RxPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject
    lateinit var viewViewModelFactory: ViewModelFactory
    private lateinit var viewModel: PageFreeWorkoutViewModel

    private var contentListAdapter: ContentListAdapter<ContentModel>? = null

    private fun initView(context: Context) {
        val recyclerView: RecyclerView = listComponent.recyclerView
        contentListAdapter = ContentListAdapter(AppConst.HOMET_LIST_ITEM_FREE_WORKOUT)
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

    private fun initDisposables() {
        disposables += viewModel.getFreeWorkout()
    }

    override fun getLayoutResId(): Int {
        return R.layout.page_free_workout
    }

    override fun onCreatedView() {
        Log.d(TAG, "onCreatedView() start")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageFreeWorkoutViewModel::class.java]
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
                    AppConst.LIVE_DATA_CMD_LIST -> {
                        liveData.contentModel?.let {
                            contentListAdapter?.addData(it) ?: Log.e(TAG, "adapter is null")
                        }
                    }
                    else -> Log.e(TAG, "wrong command")
                }
            }
        })
        super.onCreatedView()

        context?.let{ initView(it) }
        initDisposables()
        Log.d(TAG, "onCreatedView() end")
    }

    override fun onDestroyedView() {
        Log.d(TAG, "onDestroyedView()")
        contentListAdapter = null
        super.onDestroyedView()
    }
}