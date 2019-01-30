package com.kakaovx.homet.user.component.di.page

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.kakaovx.homet.user.component.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.skeleton.model.layoutmanager.SpanningLinearLayoutManager
import dagger.Module
import dagger.Provides

@Module
class LayoutManagerModule{

    @Provides
    fun provideHorizontalLinearLayoutManager(context:Context): HorizontalLinearLayoutManager {
        return HorizontalLinearLayoutManager(context)
    }

    @Provides
    fun provideVerticalLinearLayoutManager(context:Context): VerticalLinearLayoutManager {
        return VerticalLinearLayoutManager(context)
    }

}

class HorizontalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.HORIZONTAL, false)
class VerticalLinearLayoutManager(context: Context): SpanningLinearLayoutManager(context,  LinearLayoutManager.VERTICAL, false)