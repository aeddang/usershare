package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.view.item.*
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class HomeListAdapter<T>(private val listType: Int): MultipleAdapter<T>() {

    private val TAG = javaClass.simpleName

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        Log.d(TAG, "getViewHolder() viewType[$viewType]")
        return when(viewType) {
            AppConst.HOMET_LIST_ITEM_HOME_PROGRAM -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_PROGRAM")
                ViewHolder(HomeProgramCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_WORKOUT_TYPE -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_WORKOUT_TYPE")
                ViewHolder(ContentCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_BANNER -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_BANNER")
                ViewHolder(ContentCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_FREE_WORKOUT -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_FREE_WORKOUT")
                ViewHolder(HomeFreeWorkoutCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_TRAINER -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_TRAINER")
                ViewHolder(HomeTrainerCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_HOME_ISSUE_PROGRAM -> {
                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_HOME_ISSUE_PROGRAM")
                ViewHolder(HomeIssueProgramCardItem(parent.context))
            }
            else -> { ViewHolder(ImageItem(parent.context)) }
        }
    }

    override fun getViewType(position: Int): Int {
        return listType
    }
}