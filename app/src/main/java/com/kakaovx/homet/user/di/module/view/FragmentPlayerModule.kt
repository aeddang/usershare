package com.kakaovx.homet.user.di.module.view

import com.kakaovx.homet.user.di.annotation.FragmentScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.player.PlayerViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class FragmentPlayerModule {

    @Provides
    @FragmentScope
    fun provideViewModelFactory(repository: Repository): PlayerViewModelFactory
        = PlayerViewModelFactory(repository)
}