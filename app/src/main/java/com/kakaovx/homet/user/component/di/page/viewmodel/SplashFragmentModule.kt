package com.kakaovx.homet.user.component.di.page.viewmodel

import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.splash.SplashViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SplashFragmentModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): SplashViewModelFactory
        = SplashViewModelFactory(repository)
}