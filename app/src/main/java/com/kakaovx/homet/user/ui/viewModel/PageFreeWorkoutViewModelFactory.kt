package com.kakaovx.homet.user.ui.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kakaovx.homet.user.component.repository.Repository

class PageFreeWorkoutViewModelFactory(private val repo: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PageFreeWorkoutViewModel(repo) as T
    }
}