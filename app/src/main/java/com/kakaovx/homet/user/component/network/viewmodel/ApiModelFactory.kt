package com.kakaovx.homet.user.component.network.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kakaovx.homet.user.component.network.api.GitHubApi

class ApiModelFactory(private val restApi: GitHubApi): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GithubModel(restApi) as T
    }
}