package com.kakaovx.homet.user.component.network.module

import com.kakaovx.homet.user.component.annotation.PageScope
import com.kakaovx.homet.user.component.network.api.GitHubApi
import com.kakaovx.homet.user.component.network.viewmodel.ApiModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    @PageScope
    fun provideApiModelFactory(api: GitHubApi): ApiModelFactory {
        return ApiModelFactory(api)
    }

}