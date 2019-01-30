package com.kakaovx.homet.user.ui.page

import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.ui.module.PagePagerAdapter
import com.kakaovx.homet.user.component.ui.skeleton.view.ViewPagerPageFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.page_viewpager.*


class PageViewPager : ViewPagerPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject lateinit var setting: SettingPreference

    override fun onCreated() {
        super.onCreated()
        val adapt = PagePagerAdapter(fragmentManager).setDatas(pages)
        viewPager.adapter = adapt
        recyclerTabLayout.setUpWithViewPager(viewPager)
    }

}


