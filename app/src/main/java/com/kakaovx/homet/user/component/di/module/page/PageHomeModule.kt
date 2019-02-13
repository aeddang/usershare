package com.kakaovx.homet.user.component.di.module.page

import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.viewModel.PageHomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PageHomeModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): PageHomeViewModelFactory
        = PageHomeViewModelFactory(repository)
}