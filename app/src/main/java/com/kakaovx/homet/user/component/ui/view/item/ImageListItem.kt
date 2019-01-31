package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import kotlinx.android.synthetic.main.item_image.view.*

class ImageListItem(context:Context): ListItem(context) {

    override fun onCreated() { }

    override fun getLayoutResId(): Int {
        return R.layout.item_image
    }

    override fun <String> setData(data:String)
    {
        textView.text = data.toString()
        //Glide.with(this).load(data).into(imageView)
    }
}