package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeFreeWorkoutModel
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.item_home_free_workout.view.*

class HomeFreeWorkoutCardItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreated() { }
    override fun onDestroyed() {}

    override fun getLayoutResId(): Int {
        return R.layout.item_home_free_workout
    }

    override fun <T> setData(data: T) {
        val model = data as HomeFreeWorkoutModel
        Log.d(TAG, data)
        model.home_free_workout_description?.let { home_free_workout_description.text = it }
        model.home_free_workout_title?.let { home_free_workout_title.text = it }
        model.home_free_workout_url?.let {
            Glide.with(context)
                .load(it)
                .apply(RequestOptions().error(R.drawable.kakaovx_neo_profile_000))
                .into(home_free_workout_image)
        }
    }
}