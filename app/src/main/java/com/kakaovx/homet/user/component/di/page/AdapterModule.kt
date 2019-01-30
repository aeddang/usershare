package com.kakaovx.homet.user.component.di.page

import android.view.ViewGroup
import com.kakaovx.homet.user.component.annotation.PageScope
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.BaseAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.MultipleAdapter
import com.kakaovx.homet.user.component.ui.skeleton.model.adapter.SingleAdapter
import com.kakaovx.homet.user.ui.view.item.BannerListItem
import com.kakaovx.homet.user.component.ui.skeleton.view.ListItem
import com.kakaovx.homet.user.ui.view.item.ImageListItem
import dagger.Module
import dagger.Provides

@Module
class AdapterModule {

    @Provides
    @PageScope
    fun provideBannerAdapter(): BannerAdapter {
        return BannerAdapter()
    }

    @Provides
    @PageScope
    fun provideComponentAdapter(): ComponentAdapter {
        return ComponentAdapter()
    }
}

class BannerAdapter: SingleAdapter<String>(true,10) {
    override fun getListCell(parent: ViewGroup): ListItem {
        return ImageListItem(parent.context)
    }
}

class ComponentAdapter: MultipleAdapter<String>() {
    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.ViewHolder {
        return when(viewType) {
            0 -> { ViewHolder(ImageListItem(parent.context)) }
            1 -> { ViewHolder(BannerListItem(parent.context)) }
            else -> { ViewHolder(ImageListItem(parent.context)) }
        }
    }
    override fun getViewType( position: Int): Int {
        return position % 2
    }
}