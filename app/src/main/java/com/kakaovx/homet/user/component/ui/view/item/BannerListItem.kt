package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem

class BannerListItem(context:Context): ListItem(context) {

    override fun getLayoutResId(): Int {
        return R.layout.item_banner_list
    }

    override fun <String> setData(data:String)
    { // bannerList
    }
}