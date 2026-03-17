package com.example.reposearching.data.remote

import com.example.reposearching.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 30, // 預設一筆抓 30 個
        @Query("page") page: Int = 1
    ): SearchResponse
}
