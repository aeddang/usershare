package com.kakaovx.homet.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.component.ui.skeleton.model.adpter.BaseAdapter
import com.kakaovx.homet.component.ui.skeleton.view.ListCell


class  BannerAdapter: BaseAdapter<String>() {

    override fun getListCell(parent: ViewGroup): ListCell {
        return BannerListCell(parent.context)
    }
}