package com.kakaovx.homet.user.component.ui.skeleton.view

import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.ui.skeleton.rx.RxFrameLayout
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter

abstract class RecyclerFrameLayout : RxFrameLayout, BaseAdapter.Delegate {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    override fun getLayoutResId(): Int {
        return R.layout.ui_recycler
    }
}