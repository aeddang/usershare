package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.bumptech.glide.Glide
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.network.model.ResultData
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.item_image_card.view.*

class ImageCardItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreated() { }

    override fun getLayoutResId(): Int {
        return R.layout.item_image_card
    }

    override fun <T> setData(data: T) {
        val resultData: ResultData = data as ResultData
        Log.d(TAG, "setData() get data = [$resultData]")
        val owner = resultData.ownerData
        owner?.apply {
            avatarUrl?.let {
                Glide.with(context).load(it).into(imageView)
            } ?: Log.e(TAG, "url is null")
        }
    }
}