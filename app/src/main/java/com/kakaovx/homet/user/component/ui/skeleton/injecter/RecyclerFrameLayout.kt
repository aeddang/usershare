package com.kakaovx.homet.user.component.ui.skeleton.injecter

import android.content.Context
import android.support.annotation.CallSuper
import android.util.AttributeSet
import com.kakaovx.homet.user.R
import com.kakaovx.homet.user.component.di.ui.component.DaggerRecyclerComponent
import com.kakaovx.homet.user.component.di.ui.component.RecyclerComponent
import com.kakaovx.homet.user.component.di.ui.module.*
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter

abstract class RecyclerFrameLayout : InjectableFrameLayout, BaseAdapter.Delegate {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    lateinit var component: RecyclerComponent

    override fun getLayoutResId(): Int {
        return R.layout.ui_recycler
    }

    @CallSuper
    override fun inject() {
        component = DaggerRecyclerComponent.builder()
            .adapterModule(AdapterModule())
            .layoutManagerModule(LayoutManagerModule(context))
            .build()
    }

}