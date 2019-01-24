package com.kakaovx.homet.component.ui.module

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.kakaovx.homet.component.annotation.ComponentScope
import com.kakaovx.homet.component.ui.skeleton.model.adapter.SingleAdapter
import com.kakaovx.homet.component.ui.skeleton.model.layoutmanager.SpanningLinearLayoutManager
import com.kakaovx.homet.component.ui.skeleton.view.ListCell
import dagger.Module
import dagger.Provides

@Module
class LayoutManagerModule (private val context:Context){

    @Provides
    @ComponentScope
    fun provideHorizontalLinearLayoutManager(): HorizontalLinearLayoutManager {
        return HorizontalLinearLayoutManager(context)
    }

    @Provides
    @ComponentScope
    fun provideVerticalLinearLayoutManager(): VerticalLinearLayoutManager {
        return VerticalLinearLayoutManager(context)
    }

}
class HorizontalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false)
class VerticalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)