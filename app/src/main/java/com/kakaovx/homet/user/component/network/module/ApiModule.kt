package com.kakaovx.homet.user.component.network.module

import com.kakaovx.homet.user.component.annotation.PageScope
import com.kakaovx.homet.user.component.network.api.GitHubApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ApiModule {

    @Provides
    @PageScope
    fun provideGitHubApi(retrofit: Retrofit): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }

}