package com.kakaovx.homet.user.component.ui.module

import android.view.ViewGroup
import com.kakaovx.homet.user.component.annotation.ComponentScope
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.SingleAdapter
import com.kakaovx.homet.user.component.ui.view.BannerListCell
import com.kakaovx.homet.user.component.ui.skeleton.view.ListCell
import dagger.Module
import dagger.Provides

@Module
class AdapterModule {
    @Provides
    @ComponentScope
    fun provideAdapterUtil(): AdapterUtil {
        return AdapterUtil(10)
    }

    @Provides
    @ComponentScope
    fun provideBannerAdapter(adapterUtil: AdapterUtil): BannerAdapter {
        return BannerAdapter()
    }

    @Provides
    @ComponentScope
    fun provideComponentAdapter(adapterUtil: AdapterUtil): ComponentAdapter {
        return ComponentAdapter()
    }
}

data class AdapterUtil(val size:Int)

class BannerAdapter: SingleAdapter<String>(true,10) {
    override fun getListCell(parent: ViewGroup): ListCell {
        return ImageListCell(parent.context)
    }
}

class ComponentAdapter: MultipleAdapter<String>() {
    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        return when(viewType) {
            0 -> { ViewHolder(ImageListCell(parent.context)) }
            1 -> { ViewHolder(BannerListCell(parent.context)) }
            else -> { ViewHolder(ImageListCell(parent.context)) }
        }
    }
    override fun getViewType( position: Int): Int {
        return position % 2
    }
}