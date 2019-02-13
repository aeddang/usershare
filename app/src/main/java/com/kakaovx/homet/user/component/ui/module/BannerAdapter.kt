package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.SingleAdapter
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.component.ui.view.item.ImageItem

class BannerAdapter: SingleAdapter<String>(true, 10) {
    override fun getListCell(parent: ViewGroup): ListItem {
        return ImageItem(parent.context)
    }
}