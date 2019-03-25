package com.kakaovx.homet.user.di.module.view

import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.page.content.trainer.PageTrainerViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PageTrainerModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): PageTrainerViewModelFactory
        = PageTrainerViewModelFactory(repository)
}