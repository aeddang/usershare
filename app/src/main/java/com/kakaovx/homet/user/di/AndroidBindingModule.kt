package com.kakaovx.homet.user.di

import com.kakaovx.homet.user.di.annotation.ActivityScope
import com.kakaovx.homet.user.di.annotation.FragmentScope
import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.di.module.view.*
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.page.content.PageHome
import com.kakaovx.homet.user.ui.page.content.program.PageProgram
import com.kakaovx.homet.user.ui.page.content.recommend.PageHomeIssueProgramList
import com.kakaovx.homet.user.ui.page.content.trainer.PageTrainer
import com.kakaovx.homet.user.ui.page.content.workout.PageFreeWorkout
import com.kakaovx.homet.user.ui.page.content.workout.detail.PageContentDetail
import com.kakaovx.homet.user.ui.page.player.PopupPlayer
import com.kakaovx.homet.user.ui.oldPlayer.PlayerActivity
import com.kakaovx.homet.user.ui.oldPlayer.PlayerFragment
import com.kakaovx.homet.user.ui.splash.SplashFragment
import com.kakaovx.homet.user.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AndroidBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindSplashActivity(): SplashActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        FragmentSplashModule::class
    ])
    internal abstract fun bindSplashFragment(): SplashFragment

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindPlayerActivity(): PlayerActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [
        FragmentPlayerModule::class
    ])
    internal abstract fun bindPlayerFragment(): PlayerFragment

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindMainActivity(): MainActivity

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageHomeModule::class
    ])
    internal abstract fun bindPageHome(): PageHome

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageHomeIssueProgramListModule::class
    ])
    internal abstract fun bindPageHomeIssueList(): PageHomeIssueProgramList

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
        PageContentDetailModule::class
    ])
    internal abstract fun bindPageContentDetail(): PageContentDetail

    @PageScope
    @ContributesAndroidInjector(modules = [
        PageTrainerModule::class
    ])
    internal abstract fun bindPageTrainer(): PageTrainer


    @PageScope
    @ContributesAndroidInjector( modules = [ PopupPlayerModule::class , PageModule::class])
    internal abstract fun bindPopupPlayer(): PopupPlayer
}