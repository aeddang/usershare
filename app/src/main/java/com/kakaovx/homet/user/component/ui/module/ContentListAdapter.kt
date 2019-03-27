package com.kakaovx.homet.user.component.ui.module

import android.view.MotionEvent
import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.view.item.ContentCardItem
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.util.Log

class ContentListAdapter<T>(private val listType: Int): MultipleAdapter<T>() {

    private val TAG = javaClass.simpleName

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
//        Log.d(TAG, "getViewHolder() viewType[$viewType]")
        return when(viewType) {
            AppConst.HOMET_LIST_ITEM_PROGRAM -> {
//                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_PROGRAM")
                ViewHolder(ContentCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_WORKOUT -> {
//                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_WORKOUT")
                ViewHolder(ContentCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_FREE_WORKOUT -> {
//                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_FREE_WORKOUT")
                ViewHolder(ContentCardItem(parent.context))
            }
            AppConst.HOMET_LIST_ITEM_TRAINER -> {
//                Log.d(TAG, "getViewHolder() create HOMET_LIST_ITEM_TRAINER")
                ViewHolder(ContentCardItem(parent.context))
            }
            else -> {
//                Log.d(TAG, "getViewHolder() create default list")
                ViewHolder(ContentCardItem(parent.context))
            }
        }
    }

    override fun getViewType(position: Int): Int {
        return listType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when(listType) {
            AppConst.HOMET_LIST_ITEM_FREE_WORKOUT -> {
//                Log.d(TAG, "onBindViewHolder() HOMET_LIST_ITEM_FREE_WORKOUT")
                holder.itemView.setOnTouchListener{ _, motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {}
                        MotionEvent.ACTION_UP -> {
                            Log.d(TAG, "onBindViewHolder() HOMET_LIST_ITEM_FREE_WORKOUT onTouch UP")
                            itemPosition.value = position
                        }
                    }
                    true
                }
            }
            else -> {
                Log.d(TAG, "onBindViewHolder() create default list")
            }
        }
    }
}