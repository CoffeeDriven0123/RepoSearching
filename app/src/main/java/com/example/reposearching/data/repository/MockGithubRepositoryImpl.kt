package com.example.reposearching.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asPagingSourceFactory
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.data.model.RepoOwner
import com.example.reposearching.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

/**
 * 假資料的 Repository 實作，用 Pager 產生 PagingData flow。
 */
class MockGithubRepositoryImpl @Inject constructor() : GithubRepository {

    override fun searchRepositories(query: String): Flow<PagingData<GithubRepo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                createMockPagingSource(query)
            }
        ).flow
    }

    private fun createMockPagingSource(query: String): PagingSource<Int, GithubRepo> {
        return when (query) {
            "error" -> ErrorPagingSource(Exception("API_ERROR_SIMULATION"))
            "network" -> ErrorPagingSource(IOException("NO_NETWORK_SIMULATION"))
            "empty" -> listOf<GithubRepo>().asPagingSourceFactory().invoke()
            else -> {
                val fakeData = List(50) { index ->
                    GithubRepo(
                        name = "Mock Repo $index ($query)",
                        stargazersCount = 1000 - index,
                        htmlUrl = "https://github.com/mock/repo-$index",
                        owner = RepoOwner(avatarUrl = "https://picsum.photos/id/${index % 100}/200")
                    )
                }
                fakeData.asPagingSourceFactory().invoke()
            }
        }
    }

    /**
     * 專門用來模擬錯誤的 PagingSource
     */
    private class ErrorPagingSource(
        private val exception: Exception
    ) : PagingSource<Int, GithubRepo>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> {
            return LoadResult.Error(exception)
        }

        override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? = null
    }
}
