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

    override fun getItem(position: Int): Fragment {
        Log.d(TAG, "getItem = [$position]")
        getPageTitle(position)?.let {
            return PageHomeRecommendList.newInstance(it.toString())
        } ?: Log.e(TAG, "no title page")
        return PageHomeRecommendList.newInstance(AppConst.HOMET_VALUE_NOTITLE)
    }

    override fun getCount(): Int {
        return AppFeature.APP_FEATURE_VIEWPAGER_COUNT
    }
}
