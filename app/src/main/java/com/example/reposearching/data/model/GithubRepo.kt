package com.example.reposearching.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 代表 GitHub Search API 回傳的最外層結果
 */
@Serializable
data class SearchResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("incomplete_results")
    val incompleteResults: Boolean,
    @SerialName("items")
    val items: List<GithubRepo>
)

/**
 * 代表單一的 GitHub Repository 資料
 */
@Serializable
data class GithubRepo(
    // Repo 名稱
    @SerialName("name")
    val name: String,
    
    // 星數
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    
    // Repo 網址
    @SerialName("html_url")
    val htmlUrl: String,
    
    // 擁有者資訊 (包含頭像)
    @SerialName("owner")
    val owner: RepoOwner
)

/**
 * 代表 Repository 的擁有者
 */
@Serializable
data class RepoOwner(
    // 擁有者頭像
    @SerialName("avatar_url")
    val avatarUrl: String
)
