package com.kakaovx.homet.user.di.module.view

import android.app.Activity
import com.kakaovx.homet.user.di.annotation.ActivityScope
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.splash.SplashActivity
import dagger.Binds
import dagger.Module

@Module
abstract class SplashActivityModule {
    @Binds
    @ActivityScope
    internal abstract fun activity(splashActivity: SplashActivity): Activity
}