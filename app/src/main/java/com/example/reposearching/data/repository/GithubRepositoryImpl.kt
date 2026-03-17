package com.example.reposearching.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.data.remote.GithubApi
import com.example.reposearching.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository 的實際實作。
 */
class GithubRepositoryImpl @Inject constructor(
    private val api: GithubApi
) : GithubRepository {

    override fun searchRepositories(query: String): Flow<PagingData<GithubRepo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(api, query) }
        ).flow
    }
}
