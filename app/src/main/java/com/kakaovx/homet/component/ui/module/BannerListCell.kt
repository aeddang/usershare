package com.kakaovx.homet.component.ui.module

import android.content.Context
import com.bumptech.glide.Glide
import com.kakaovx.homet.R
import com.kakaovx.homet.component.ui.skeleton.view.ListCell
import kotlinx.android.synthetic.main.cell_banner.view.*

class BannerListCell(context:Context): ListCell(context) {

    override fun getLayoutResId(): Int {
        return R.layout.cell_banner
    }
    override fun <String> setData(data:String)
    {
        Glide.with(this).load(data).into(imageView)
    }
}