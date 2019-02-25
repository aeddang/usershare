package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.bumptech.glide.Glide
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeRecommendModel
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import kotlinx.android.synthetic.main.item_home_recommend.view.*

class HomeRecommendCardItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreated() { }
    override fun onDestroyed() {}

    override fun getLayoutResId(): Int {
        return R.layout.item_home_recommend
    }

    override fun <T> setData(data: T) {
        val model = data as HomeRecommendModel
        model.home_recommend_name?.let { home_recommend_name.text = it }
        model.home_recommend_description?.let { home_recommend_description.text = it }
        model.home_recommend_url?.let { Glide.with(context).load(it).into(home_recommend_image) }
    }
}