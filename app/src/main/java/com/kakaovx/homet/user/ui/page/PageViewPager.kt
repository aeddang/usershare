package com.kakaovx.homet.user.ui.page

import com.kakaovx.homet.user.component.di.page.PagePagerAdapter
import com.kakaovx.homet.user.component.network.api.GitHubApi
import com.kakaovx.homet.user.component.network.model.ApiResponse
import com.kakaovx.homet.user.component.preference.SettingPreference
import com.kakaovx.homet.user.component.ui.skeleton.view.ViewPagerPageFragment
import com.kakaovx.homet.user.util.Log
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlinx.android.synthetic.main.page_viewpager.*


class PageViewPager : ViewPagerPageFragment() {

    private val TAG = javaClass.simpleName

    @Inject lateinit var viewAdapter: PagePagerAdapter
    @Inject lateinit var api:GitHubApi
    @Inject lateinit var setting: SettingPreference

    override fun onCreated() {
        super.onCreated()
    }

    override fun onInject() {
        AndroidSupportInjection.inject(this)
        val adapt = viewAdapter.setDatas(pages)
        viewPager.adapter = adapt
        recyclerTabLayout.setUpWithViewPager(viewPager)
    }

    private fun handleComplete(data: ApiResponse) {
        Log.i(TAG, "handleComplete"+ data.toString())
    }

    private fun handleError(err: Throwable) {
        Log.i(TAG, "handleError ($err)")
    }
}


