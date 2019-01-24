package com.kakaovx.homet.component.ui.module

import com.kakaovx.homet.component.annotation.ComponentScope
import com.kakaovx.homet.component.ui.skeleton.model.data.InfinityPaginationData
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    @ComponentScope
    fun provideBannerInfinityPaginationData(): InfinityPaginationData<String> {
        return InfinityPaginationData()
    }
}
