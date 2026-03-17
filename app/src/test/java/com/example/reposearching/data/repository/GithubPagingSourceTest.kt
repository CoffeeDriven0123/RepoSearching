package com.example.reposearching.data.repository

import android.util.Log
import androidx.paging.PagingSource
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.data.model.RepoOwner
import com.example.reposearching.data.model.SearchResponse
import com.example.reposearching.data.remote.GithubApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GithubPagingSourceTest {

    private val mockApi = mockk<GithubApi>()

    @Before
    fun setup() {
        // 因為程式碼中使用了 android.util.Log，在單元測試中必須 Mock 它
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        // 測試結束後取消 Mock，避免影響其他測試
        unmockkStatic(Log::class)
    }

    @Test
    fun `load returns Page when api successful`() = runTest {
        // Arrange
        val fakeRepo = GithubRepo(
            name = "Test Repo",
            stargazersCount = 100,
            htmlUrl = "https://github.com/test",
            owner = RepoOwner(avatarUrl = "https://avatar.url")
        )
        val fakeResponse = SearchResponse(
            totalCount = 1,
            incompleteResults = false,
            items = listOf(fakeRepo)
        )
        
        // 模擬 API 呼叫，傳入預設的每頁數量 30
        coEvery { mockApi.searchRepositories("test", 30, 1) } returns fakeResponse

        val pagingSource = GithubPagingSource(mockApi, "test")

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        // Assert
        val expected = PagingSource.LoadResult.Page(
            data = listOf(fakeRepo),
            prevKey = null,
            nextKey = 2 // 1 + (30/30) = 2
        )
        
        assertEquals(expected, result)
    }

    @Test
    fun `load returns Error when network fails`() = runTest {
        // Arrange
        val exception = IOException("Network Error")
        coEvery { mockApi.searchRepositories("test", 30, 1) } throws exception

        val pagingSource = GithubPagingSource(mockApi, "test")

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        // Assert
        val actualResult = result as PagingSource.LoadResult.Error
        assertEquals(exception.message, actualResult.throwable.message)
    }

    @Test
    fun `load returns Page with null nextKey when repos is empty`() = runTest {
        // Arrange
        val emptyResponse = SearchResponse(
            totalCount = 0,
            incompleteResults = false,
            items = emptyList()
        )
        coEvery { mockApi.searchRepositories("empty", 30, 1) } returns emptyResponse

        val pagingSource = GithubPagingSource(mockApi, "empty")

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 30,
                placeholdersEnabled = false
            )
        )

        // Assert
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(null, pageResult.nextKey)
    }
}
