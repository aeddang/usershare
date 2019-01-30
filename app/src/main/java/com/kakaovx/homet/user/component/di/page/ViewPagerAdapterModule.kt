package com.kakaovx.homet.user.component.di.page

import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseViewPagerAdapter
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.ui.view.item.ImageListItem

import dagger.Module
import dagger.Provides

@Module
class ViewPagerAdapterModule {

    @Provides
    fun provideBannerPagerAdapter(): BannerPagerAdapter {
        return BannerPagerAdapter()
    }
}

class BannerPagerAdapter: BaseViewPagerAdapter<ListItem, String>() {
    override fun getPageView(container: ViewGroup, position: Int): ListItem {
        return ImageListItem(container.context)
    }
}


