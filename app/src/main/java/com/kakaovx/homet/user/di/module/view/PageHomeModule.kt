package com.kakaovx.homet.user.di.module.view

import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.page.content.PageHomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PageHomeModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): PageHomeViewModelFactory
        = PageHomeViewModelFactory(repository)
}