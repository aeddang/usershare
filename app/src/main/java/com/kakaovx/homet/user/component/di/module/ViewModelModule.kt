package com.kakaovx.homet.user.component.di.module

import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.network.viewmodel.ApiModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    @PageScope
    fun provideApiModelFactory(api: RestfulApi): ApiModelFactory {
        return ApiModelFactory(api)
    }

}