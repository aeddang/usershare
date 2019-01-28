package com.kakaovx.homet.user.component.network.viewmodel

import android.arch.lifecycle.ViewModel
import com.kakaovx.homet.user.component.network.RxObservableConverter
import com.kakaovx.homet.user.component.network.api.GitHubApi
import com.kakaovx.homet.user.component.network.model.ApiResponse
import io.reactivex.Observable


class GithubModel(private val api: GitHubApi): ViewModel() {

    fun getSearchRepositories(): Observable<ApiResponse> {
        val params: MutableMap<String, String> = mutableMapOf()
        params["q"] = "apple"
        api.searchRepositories(params)
        return RxObservableConverter.forNetwork(api.searchRepositories(params))

    }
}