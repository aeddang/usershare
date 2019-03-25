package com.kakaovx.homet.user.ui.page.content.trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kakaovx.homet.user.component.repository.Repository

class PageTrainerViewModelFactory(private val repo: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PageTrainerViewModel(repo) as T
    }
}