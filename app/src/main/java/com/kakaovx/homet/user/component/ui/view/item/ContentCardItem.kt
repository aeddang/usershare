package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.ContentModel
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import kotlinx.android.synthetic.main.item_content_card.view.*

class ContentCardItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreatedView() { }
    override fun onDestroyedView() {}

    override fun getLayoutResId(): Int {
        return R.layout.item_content_card
    }

    override fun <T> setData(data: T) {
        val model: ContentModel = data as ContentModel
        content_card_title.text = model.title ?: "card name null"
        content_card_description.text = model.description ?: "card description null"
        model.image_url?.let {
            Glide.with(context)
                    .load(it)
                    .apply(RequestOptions().error(R.drawable.kakaovx_ryan_mono))
                    .into(content_card_image)
        }
    }
}