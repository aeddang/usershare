package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeTrainerModel
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.item_home_trainer.view.*

class HomeTrainerCardItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreated() { }
    override fun onDestroyed() {}

    override fun getLayoutResId(): Int {
        return R.layout.item_home_trainer
    }

    override fun <T> setData(data: T) {
        val model = data as HomeTrainerModel
        Log.d(TAG, data)
        model.home_trainer_name?.let { home_trainer_name.text = it }
        model.home_trainer_description?.let { home_trainer_description.text = it }
        model.home_trainer_job?.let { home_trainer_job.text = it }
        model.home_trainer_url?.let {
            Glide.with(context)
                 .load(it)
                 .apply(RequestOptions().error(R.drawable.kakaovx_apeach_profile_000))
                 .into(home_trainer_image)
        }
    }
}