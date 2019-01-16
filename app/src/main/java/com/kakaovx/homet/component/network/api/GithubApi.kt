package com.kakaovx.homet.component.network.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.*

interface GitHubApi {

    @GET("/search/repositories")
    fun searchRepositories(@QueryMap params: Map<String, String>): Observable<Objects>

}