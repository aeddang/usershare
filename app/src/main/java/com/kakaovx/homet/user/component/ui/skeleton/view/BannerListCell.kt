package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.ListCell

class BannerListCell(context:Context): ListCell(context) {

    override fun getLayoutResId(): Int {
        return R.layout.cell_banner_list
    }
    override fun <String> setData(data:String)
    {
        // bannerList
    }
}