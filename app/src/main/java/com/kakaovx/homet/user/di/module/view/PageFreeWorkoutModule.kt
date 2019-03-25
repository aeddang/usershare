package com.kakaovx.homet.user.di.module.view

import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.ui.viewModel.PageFreeWorkoutViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PageFreeWorkoutModule {

    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): PageFreeWorkoutViewModelFactory
        = PageFreeWorkoutViewModelFactory(repository)
}