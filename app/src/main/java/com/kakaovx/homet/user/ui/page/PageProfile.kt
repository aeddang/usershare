package com.kakaovx.homet.user.ui.page

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.util.Log


class PageProfile : RxPageFragment() {

    private val TAG = javaClass.simpleName

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_profile)
                setDisplayShowTitleEnabled(true)
            }
        }
    }

    override fun getLayoutResId(): Int { return R.layout.page_profile }

    override fun onCreated() {
        super.onCreated()
        Log.d(TAG, "onCreated()")

        context?.let{ initView(it) }
    }
}