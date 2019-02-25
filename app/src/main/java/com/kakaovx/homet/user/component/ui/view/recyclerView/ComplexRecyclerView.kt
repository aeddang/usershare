package com.kakaovx.homet.user.component.ui.view.recyclerView

import android.content.Context
import android.util.AttributeSet
import com.kakaovx.homet.user.component.ui.skeleton.view.RecyclerFrameLayout
import com.kakaovx.homet.user.util.Log

class ComplexRecyclerView: RecyclerFrameLayout {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onCreated() {
        Log.d("ComplexRecyclerView", "onCreated()")
    }
    override fun onDestroyed() {
        Log.d("ComplexRecyclerView", "onDestroyed()")
    }
}