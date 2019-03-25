package com.kakaovx.homet.user.ui.page.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kakaovx.homet.user.component.repository.Repository

class PageHomeViewModelFactory(private val repo: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PageHomeViewModel(repo) as T
    }
}