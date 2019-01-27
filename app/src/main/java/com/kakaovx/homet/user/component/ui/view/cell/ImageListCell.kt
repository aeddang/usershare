package com.kakaovx.homet.user.component.ui.view.cell

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.ListCell
import kotlinx.android.synthetic.main.cell_image.view.*

class ImageListCell(context:Context): ListCell(context) {

    override fun getLayoutResId(): Int {
        return R.layout.cell_image
    }
    override fun <String> setData(data:String)
    {
        textView.text = data.toString()
        //Glide.with(this).load(data).into(imageView)
    }
}