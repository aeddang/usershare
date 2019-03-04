package com.kakaovx.homet.user.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kakaovx.homet.user.component.repository.Repository

class PageHomeIssueProgramListViewModelFactory(private val repo: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PageHomeIssueProgramListViewModel(repo) as T
    }
}