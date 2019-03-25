package com.kakaovx.homet.user.component.ui.module

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.ui.page.content.recommend.PageHomeIssueProgramList

class HomePagerAdapter(val context: Context,
                       fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private val TAG = javaClass.simpleName

    private val tagList: ArrayList<String> = ArrayList()

    fun setDataList(list: ArrayList<String>) {
        tagList.clear()
        tagList.addAll(list)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tagList[position]
    }

    override fun getItem(position: Int): Fragment {
        return PageHomeIssueProgramList.newInstance(tagList[position])
    }

    override fun getCount(): Int {
        val count = tagList.size
        if (count < AppFeature.APP_FEATURE_VIEWPAGER_COUNT) {
            return tagList.size
        } else {
            return AppFeature.APP_FEATURE_VIEWPAGER_COUNT
        }
    }
}
