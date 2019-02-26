package com.kakaovx.homet.user.component.ui.module

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kakaovx.homet.user.constant.AppConst
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.ui.page.PageHomeRecommendList
import com.kakaovx.homet.user.util.Log

class HomePagerAdapter(val context: Context,
                       fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private val TAG = javaClass.simpleName

    private val tagList: ArrayList<String> = ArrayList()

    fun setDataList(tagList: ArrayList<String>) {
        tagList.addAll(tagList)
    }

    override fun getItem(position: Int): Fragment {
        Log.d(TAG, "getItem = [$position]")
//        return PageHomeRecommendList.newInstance(tagList[position])
        return PageHomeRecommendList.newInstance(AppConst.HOMET_VALUE_NOTITLE)
    }

    override fun getCount(): Int {
        return AppFeature.APP_FEATURE_VIEWPAGER_COUNT
    }
}
