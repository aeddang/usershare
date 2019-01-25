package com.kakaovx.homet.component.ui.view

import android.content.Context
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.view.ListCell
import kotlinx.android.synthetic.main.cell_banner_list.view.*

class BannerListCell(context:Context): ListCell(context) {

    override fun getLayoutResId(): Int {
        return R.layout.cell_banner_list
    }
    override fun <String> setData(data:String)
    {
        // bannerList
    }
}