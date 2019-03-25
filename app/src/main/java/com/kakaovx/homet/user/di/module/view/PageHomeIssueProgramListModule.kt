package com.kakaovx.homet.user.di.module.view

import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.page.content.recommend.PageHomeIssueProgramListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PageHomeIssueProgramListModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): PageHomeIssueProgramListViewModelFactory
        = PageHomeIssueProgramListViewModelFactory(repository)
}