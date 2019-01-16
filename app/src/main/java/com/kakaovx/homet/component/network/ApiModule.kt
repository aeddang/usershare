package com.kakaovx.homet.component.network

import com.kakaovx.homet.component.annotation.PageScope
import com.kakaovx.homet.component.network.api.GitHubApi
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