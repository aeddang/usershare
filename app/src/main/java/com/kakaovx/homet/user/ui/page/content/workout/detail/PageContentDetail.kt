package com.kakaovx.homet.user.ui.page.content.workout.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.network.model.WorkoutData
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.databinding.PageContentDetailBinding
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.ParamType
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.page_home.*
import javax.inject.Inject

class PageContentDetail : RxPageFragment() {

    private val TAG = javaClass.simpleName

    private lateinit var dataBinding: PageContentDetailBinding
    private var dataId: String? = null

    @Inject
    lateinit var viewViewModelFactory: PageContentDetailViewModelFactory
    private lateinit var viewModel: PageContentDetailViewModel

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_content)
                setDisplayShowTitleEnabled(true)
            }
        }
    }

    private fun initComponent(data: WorkoutData) {
        dataBinding.apply {
            exerciseIdText.text = data.exercise_id
            titleText.text = data.title
            descriptionText.text = data.description
            bodyPartsText.text = data.body_parts
            calorieText.text = data.calorie
            playTimeText.text = data.play_time
            motionCountText.text = data.motion_count
            programCountText.text = data.program_count
            confirmStatusText.text = data.confirm_status
            isDisplayText.text = data.is_display
            readCountText.text = data.read_count
            providerIdText.text = data.provider_id
            addTimeText.text = data.add_time
            modifyTimeText.text = data.modify_time
            movieUrlText.text = data.movie_url
            thumbnailUrlText.text = data.thumb_url
            freeMotionIdText.text = data.free_motion_id
            freeMotionMovieUrlText.text = data.free_motion_movie_url
            freeMotionThumbnailUrlText.text = data.free_motion_thumb_url
            playWorkoutAction.setOnClickListener {
                val id = freeMotionIdText.text.toString()
                val url = freeMotionMovieUrlText.text.toString()
                if (id.isNotEmpty() && url.isNotEmpty()) {
                    routeToPlayer(id, url)
                }
            }
        }
    }

    private fun routeToPlayer(motion_id: String, movie_url: String) {
        Log.d(TAG, "routeToPlayer() motion_id = [$motion_id], movie_url = [$movie_url]")
        val i = Intent(AppConst.HOMET_ACTIVITY_PLAYER)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        i.putExtra(AppConst.HOMET_VALUE_MOTION_ID, motion_id)
        i.putExtra(AppConst.HOMET_VALUE_VIDEO_URL, movie_url)
        startActivity(i)
        activity?.finish()
    }

    @LayoutRes
    override fun getLayoutResId(): Int = R.layout.page_content_detail

    override fun setParam(param: Map<String, Any>): PageFragment {
        dataId = param[ParamType.DETAIL.key] as String?
        Log.d(TAG, "setParam() data = [$dataId]")
        return this
    }

    override fun onSubscribe() {
        dataId?.let {
            disposables += viewModel.getWorkoutContent(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView()")
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.page_content_detail, container, false)
        return dataBinding.root
    }

    override fun onCreated() {
        Log.d(TAG, "onCreated()")
        AndroidSupportInjection.inject(this)

        viewModel = ViewModelProviders.of(this, viewViewModelFactory)[PageContentDetailViewModel::class.java]

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
                    else -> Log.e(TAG, "wrong command")
                }
            } ?: Log.e(TAG, "liveData is null")
        })

        viewModel.content.observe(this, Observer { workoutData ->
            workoutData?.let {
//                Log.d(TAG, "workoutData = [$workoutData]")
                initComponent(it)
            }
        })

        context?.let{ initView(it) }
        super.onCreated()
    }

    override fun onDestroyed() {
        Log.d(TAG, "onDestroyed()")
        super.onDestroyed()
    }
}


