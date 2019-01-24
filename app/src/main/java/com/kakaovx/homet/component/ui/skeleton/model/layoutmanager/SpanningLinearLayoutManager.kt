package com.kakaovx.homet.component.ui.skeleton.model.layoutmanager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewGroup


open class SpanningLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateDefaultLayoutParams())
    }

    override fun generateLayoutParams(c: Context, attrs: AttributeSet): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(c, attrs))
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams): RecyclerView.LayoutParams {
        return spanLayoutSize(super.generateLayoutParams(lp))
    }

    protected fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        return layoutParams
    }

}