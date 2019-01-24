package com.kakaovx.homet.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.component.annotation.ComponentScope
import com.kakaovx.homet.component.ui.skeleton.model.adpter.SingleAdapter
import com.kakaovx.homet.component.ui.skeleton.view.ListCell
import dagger.Module
import dagger.Provides

@Module
class AdapterModule {

    @Provides
    @ComponentScope
    fun provideBannerAdapter(): BannerAdapter {
        return BannerAdapter()
    }
}

class BannerAdapter: SingleAdapter<String>(10, true) {
    override fun getListCell(parent: ViewGroup): ListCell {
        return ImageListCell(parent.context)
    }
}
