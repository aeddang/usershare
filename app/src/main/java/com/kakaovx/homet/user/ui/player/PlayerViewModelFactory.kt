package com.kakaovx.homet.user.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kakaovx.homet.user.component.repository.Repository
import com.kakaovx.homet.user.component.vxcore.VxCamera

class PlayerViewModelFactory(private val repo: Repository,
                             private val camera: VxCamera): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PlayerViewModel(repo, camera) as T
    }
}