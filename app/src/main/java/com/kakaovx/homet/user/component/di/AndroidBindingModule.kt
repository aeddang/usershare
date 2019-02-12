package com.kakaovx.homet.user.component.di

import com.kakaovx.homet.user.component.di.annotation.ActivityScope
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.di.module.page.PageProgramModule
import com.kakaovx.homet.user.component.di.module.page.PageFreeWorkoutModule
import com.kakaovx.homet.user.component.di.module.page.PageSplashModule
import com.kakaovx.homet.user.component.di.module.page.PageTrainerModule
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.page.*
import com.kakaovx.homet.user.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AndroidBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindSplashActivity(): SplashActivity

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageSplashModule::class
    ])
    internal abstract fun bindPageSplash(): PageSplash

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindMainActivity(): MainActivity

    @PageScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindPageMain(): PageHome

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageProgramModule::class
    ])
    internal abstract fun bindPageProgram(): PageProgram

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageFreeWorkoutModule::class
    ])
    internal abstract fun bindPageFreeWorkout(): PageFreeWorkout

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageTrainerModule::class
    ])
    internal abstract fun bindPageTrainer(): PageTrainer
}