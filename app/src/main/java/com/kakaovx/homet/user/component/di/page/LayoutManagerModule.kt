package com.kakaovx.homet.user.component.di.page

import android.content.Context
import com.kakaovx.homet.user.component.ui.module.HorizontalLinearLayoutManager
import com.kakaovx.homet.user.component.ui.module.VerticalLinearLayoutManager
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

