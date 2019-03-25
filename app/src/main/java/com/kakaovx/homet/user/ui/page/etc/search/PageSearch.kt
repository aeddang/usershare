package com.kakaovx.homet.user.ui.page.etc.search

import android.content.Context
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxPageFragment
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.util.Log
import kotlinx.android.synthetic.main.page_home.*


class PageSearch : RxPageFragment() {

    private val TAG = javaClass.simpleName

    private fun initView(context: Context) {
        activity?.let {
            val myActivity: MainActivity = activity as MainActivity
            myActivity.setSupportActionBar(toolbar)
            myActivity.supportActionBar?.apply {
                title = context.getString(R.string.page_search)
                setDisplayShowTitleEnabled(true)
            }
        }
    }

    override fun getLayoutResId(): Int { return R.layout.page_search }

    override fun onCreated() {
        super.onCreated()
        Log.d(TAG, "onCreated()")

        context?.let{ initView(it) }
    }
}