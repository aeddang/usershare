package com.kakaovx.homet.user.di

import com.kakaovx.homet.user.di.annotation.ActivityScope
import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.di.module.view.ActivityModule
import com.kakaovx.homet.user.di.module.view.FragmentPlayerModule
import com.kakaovx.homet.user.di.module.view.PageModule
import com.kakaovx.homet.user.ui.MainActivity
import com.kakaovx.homet.user.ui.oldPlayer.PlayerActivity
import com.kakaovx.homet.user.ui.oldPlayer.PlayerFragment
import com.kakaovx.homet.user.ui.page.content.recommend.PageHome
import com.kakaovx.homet.user.ui.page.content.program.PageProgram
import com.kakaovx.homet.user.ui.page.content.recommend.PageHomeIssueProgramList
import com.kakaovx.homet.user.ui.page.content.search.PageFilter
import com.kakaovx.homet.user.ui.page.content.search.PageSearch
import com.kakaovx.homet.user.ui.page.content.trainer.PageTrainer
import com.kakaovx.homet.user.ui.page.content.workout.PageFreeWorkout
import com.kakaovx.homet.user.ui.page.content.workout.detail.PageContentDetail
import com.kakaovx.homet.user.ui.page.etc.account.PageAccount
import com.kakaovx.homet.user.ui.page.etc.login.PopupLogin
import com.kakaovx.homet.user.ui.page.etc.payment.PagePayment
import com.kakaovx.homet.user.ui.page.planner.diet.PageDietPlanner
import com.kakaovx.homet.user.ui.page.planner.program.PagePlanner
import com.kakaovx.homet.user.ui.page.player.PopupPlayer
import com.kakaovx.homet.user.ui.page.profile.PageProfile
import com.kakaovx.homet.user.ui.page.profile.setting.PageSetting
import com.kakaovx.homet.user.ui.page.report.diet.PageDietReport
import com.kakaovx.homet.user.ui.page.report.program.PageProgramReport
import com.kakaovx.homet.user.ui.splash.SplashActivity
import com.kakaovx.homet.user.ui.splash.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AndroidBindingModule {

    /**
     * Splash Activity
     */

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindSplashActivity(): SplashActivity

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindSplashFragment(): SplashFragment

    /**
     * Main Activity
     */

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindMainActivity(): MainActivity

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageHome(): PageHome

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageHomeIssueList(): PageHomeIssueProgramList

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageProgram(): PageProgram

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageFreeWorkout(): PageFreeWorkout

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageContentDetail(): PageContentDetail

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageTrainer(): PageTrainer

    @PageScope
    @ContributesAndroidInjector( modules = [ PageModule::class])
    internal abstract fun bindPopupPlayer(): PopupPlayer

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageFilter(): PageFilter

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageSearch(): PageSearch

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageAccount(): PageAccount

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPagePayment(): PagePayment

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageDietPlanner(): PageDietPlanner

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageMainPlanner(): PagePlanner

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageSetting(): PageSetting

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageProfile(): PageProfile

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageDietReport(): PageDietReport

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPageProgramReport(): PageProgramReport

    @PageScope
    @ContributesAndroidInjector(modules = [ ])
    internal abstract fun bindPopupLogin(): PopupLogin

    /**
     * Old Player Activity
     * TODO : will be deprecate.
     */

    @ActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindPlayerActivity(): PlayerActivity

    @PageScope
    @ContributesAndroidInjector(modules = [ PageModule::class ])
    internal abstract fun bindPlayerFragment(): PlayerFragment

}
