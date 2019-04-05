package com.kakaovx.homet.user.di.module.view

import android.app.Activity
import com.kakaovx.homet.user.di.annotation.ActivityScope
import com.kakaovx.homet.user.ui.MainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {
    @Binds
    @ActivityScope
    internal abstract fun activity(mainActivity: MainActivity): Activity
}