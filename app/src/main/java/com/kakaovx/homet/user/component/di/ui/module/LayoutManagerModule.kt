package com.kakaovx.homet.user.component.di.ui.module

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.kakaovx.homet.user.component.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.skeleton.model.layoutmanager.SpanningLinearLayoutManager
import dagger.Module
import dagger.Provides

@Module
class LayoutManagerModule (private val context:Context){
    @Provides
    @ComponentScope
    fun provideLayoutManagerUtil(): LayoutManagerUtil {
        return LayoutManagerUtil(10)
    }

    @Provides
    @ComponentScope
    fun provideHorizontalLinearLayoutManager(layoutManagerUtil:LayoutManagerUtil): HorizontalLinearLayoutManager {
        return HorizontalLinearLayoutManager(context)
    }

    @Provides
    @ComponentScope
    fun provideVerticalLinearLayoutManager(layoutManagerUtil:LayoutManagerUtil): VerticalLinearLayoutManager {
        return VerticalLinearLayoutManager(context)
    }

}
data class LayoutManagerUtil(val size:Int)

class HorizontalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false)
class VerticalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)