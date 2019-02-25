package com.kakaovx.homet.user.component.di.module.page

import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.viewModel.PageHomeRecommendListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PageHomeRecommendListModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): PageHomeRecommendListViewModelFactory
        = PageHomeRecommendListViewModelFactory(repository)
}