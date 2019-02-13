package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseViewPagerAdapter
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.component.ui.view.item.ImageItem

class BannerPagerAdapter: BaseViewPagerAdapter<ListItem, String>() {
    override fun getPageView(container: ViewGroup, position: Int): ListItem {
        return ImageItem(container.context)
    }
}