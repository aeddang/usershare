package com.kakaovx.homet.user.di.module.view

import com.kakaovx.homet.user.di.annotation.FragmentScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.splash.SplashViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class FragmentSplashModule {

    @Provides
    @FragmentScope
    fun provideViewModelFactory(repository: Repository): SplashViewModelFactory
        = SplashViewModelFactory(repository)
}