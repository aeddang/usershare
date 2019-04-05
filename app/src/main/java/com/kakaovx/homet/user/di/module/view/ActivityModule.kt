package com.kakaovx.homet.user.di.module.view
import android.app.Activity
import com.kakaovx.homet.user.component.deeplink.DeepLinkManager
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.ui.skeleton.model.viewmodel.ViewModelFactory
import com.kakaovx.homet.user.di.annotation.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {
    @Provides
    @ActivityScope
    fun provideViewModelFactory(repository: Repository): ViewModelFactory = ViewModelFactory(repository)

    @Provides
    @ActivityScope
    fun provideDeepLinkManager (activity: Activity): DeepLinkManager = DeepLinkManager (activity)
}
