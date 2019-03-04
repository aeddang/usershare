package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeProgramModel
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.item_home_program.view.*

class HomeProgramCardItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreated() { }
    override fun onDestroyed() {}

    override fun getLayoutResId(): Int {
        return R.layout.item_home_program
    }

    override fun <T> setData(data: T) {
        val model = data as HomeProgramModel
        Log.d(TAG, data)
        model.home_program_name?.let { home_program_name.text = it }
        model.home_program_description?.let { home_program_description.text = it }
        model.home_program_url?.let {
            Glide.with(context)
                .load(it)
                .apply(RequestOptions().error(R.drawable.kakaovx_frodo_profile_000))
                .into(home_program_image)
        }
    }
}