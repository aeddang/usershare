package com.kakaovx.homet.user.component.di.page
import android.support.v4.app.Fragment
import com.kakaovx.homet.lib.page.PageFragment
import com.kakaovx.homet.user.component.annotation.PageScope
import com.kakaovx.homet.user.ui.page.PageViewPager
import dagger.Binds
import dagger.Module

@Module
abstract class FragmentModule {

    @Binds
    @PageScope
    abstract fun fragment(f: PageViewPager): Fragment
}