package com.example.reposearching.domain.usecase

import androidx.paging.PagingData
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

/**
 * 專門負責「搜尋 Repo」這個行為的 UseCase
 * 透過 @Inject constructor 讓 Hilt 能夠自動注入 GithubRepository
 */
class SearchRepoUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    operator fun invoke(query: String): Flow<PagingData<GithubRepo>> {
        if (query.isBlank()) {
            return emptyFlow()
        }
        
        return repository.searchRepositories(query)
    }
}
