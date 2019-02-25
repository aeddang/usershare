package com.kakaovx.homet.user.component.ui.view.viewpager

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxFrameLayout
import com.kakaovx.homet.user.util.Log

class RecommendViewPager: RxFrameLayout, BaseAdapter.Delegate {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun onCreated() {
        Log.d("RecommendViewPager", "onCreated()")
    }

    override fun onDestroyed() {
        Log.d("RecommendViewPager", "onDestroyed()")
    }

    @LayoutRes
    override fun getLayoutResId() = R.layout.ui_viewpager
}