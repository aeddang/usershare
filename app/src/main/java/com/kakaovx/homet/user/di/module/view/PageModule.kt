package com.kakaovx.homet.user.di.module.view
import com.kakaovx.homet.user.di.annotation.PageScope
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PageModule {
    @Provides
    @PageScope
    fun provideViewModelFactory(repository: Repository): ViewModelFactory = ViewModelFactory(repository)
}
