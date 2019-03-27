package com.kakaovx.homet.user.component.ui.view.viewpager

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxFrameLayout
import com.kakaovx.homet.user.util.Log

class IssueProgramViewPager: RxFrameLayout, BaseAdapter.Delegate {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun onCreatedView() {
        Log.d("IssueProgramViewPager", "onCreatedView()")
    }

    override fun onDestroyedView() {
        Log.d("IssueProgramViewPager", "onDestroyedView()")
    }

    @LayoutRes
    override fun getLayoutResId() = R.layout.ui_viewpager
}