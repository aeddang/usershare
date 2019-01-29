package com.kakaovx.homet.user.component.di.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseViewPagerAdapter
import com.kakaovx.homet.user.component.di.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.component.ui.view.item.ImageListItem

import dagger.Module
import dagger.Provides

@Module
class PagerAdapterModule {
    @Provides
    @ComponentScope
    fun providePagerAdapterUtil(): PagerAdapterUtil {
        return PagerAdapterUtil(10)
    }

    @Provides
    @ComponentScope
    fun provideBannerPagerAdapter(adapterUtil: PagerAdapterUtil): BannerPagerAdapter {
        return BannerPagerAdapter()
    }

}

data class PagerAdapterUtil(val size:Int)


class BannerPagerAdapter: BaseViewPagerAdapter<ListItem, String>() {
    override fun getPageView(container: ViewGroup, position: Int): ListItem {
        return ImageListItem(container.context)
    }

}


