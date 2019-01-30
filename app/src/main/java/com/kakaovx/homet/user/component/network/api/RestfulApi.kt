package com.kakaovx.homet.user.component.network.api

import com.kakaovx.homet.user.component.network.model.ApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestfulApi {

    @GET("/search/repositories")
    fun searchRepositories(@QueryMap params: Map<String, String>): Observable<ApiResponse>

}