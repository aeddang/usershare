package com.kakaovx.homet.user.di.module.view

import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.ui.oldPlayer.PlayerViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class FragmentPlayerModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): PlayerViewModelFactory
        = PlayerViewModelFactory(repository)
}