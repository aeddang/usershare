package com.kakaovx.homet.user.component.ui.view.item

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.model.HomeIssueProgramModel
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.item_home_issue_program.view.*

class HomeIssueProgramCardItem(context:Context): ListItem(context) {

    private val TAG = javaClass.simpleName

    override fun onCreatedView() { }
    override fun onDestroyedView() {}

    override fun getLayoutResId(): Int {
        return R.layout.item_home_issue_program
    }

    override fun <T> setData(data: T) {
        val model = data as HomeIssueProgramModel
        Log.d(TAG, data)
        model.home_issue_program_name?.let { home_issue_program_name.text = it }
        model.home_issue_program_description?.let { home_issue_program_description.text = it }
        model.home_issue_program_url?.let {
            Glide.with(context)
                .load(it)
                .apply(RequestOptions().error(R.drawable.kakaovx_muzi_profile_000))
                .into(home_issue_program_image)
        }
    }
}