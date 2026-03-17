package com.example.reposearching.domain.repository

import androidx.paging.PagingData
import com.example.reposearching.data.model.GithubRepo
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    /**
     * 根據關鍵字搜尋 GitHub Repo，回傳 PagingData 的資料流
     */
    fun searchRepositories(query: String): Flow<PagingData<GithubRepo>>
}
