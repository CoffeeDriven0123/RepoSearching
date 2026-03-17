package com.example.reposearching.ui.viewmodel

import androidx.paging.PagingData
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.domain.usecase.SearchRepoUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val mockUseCase = mockk<SearchRepoUseCase>()
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // 確保 Main Dispatcher 使用我們的測試 Dispatcher
        Dispatchers.setMain(testDispatcher)
        
        // 預設 UseCase 行為：不論輸入什麼都回傳空的 Flow
        every { mockUseCase(any()) } returns flowOf(PagingData.empty())
        
        viewModel = SearchViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search triggers UseCase after debounce delay`() = runTest(testDispatcher) {
        // Arrange
        val query = "compose"
        every { mockUseCase(query) } returns flowOf(PagingData.empty())

        // 開始收集 flow (PagingData 需要被 collect 才會觸發 flatMapLatest)
        val job = launch {
            viewModel.pagingDataFlow.collect { }
        }

        // Act
        viewModel.search(query)
        
        // [關鍵]：因為有 debounce(500ms)，我們先驗證目前還沒被呼叫
        verify(exactly = 0) { mockUseCase(any()) }
        
        // 推進時間超過 500ms
        advanceTimeBy(600)
        
        // Assert
        verify(exactly = 1) { mockUseCase(query) }
        
        job.cancel()
    }

    @Test
    fun `search with blank query does not trigger UseCase`() = runTest(testDispatcher) {
        // Act
        viewModel.search("   ")
        advanceTimeBy(600)

        // Assert
        // 在新邏輯中，空白字串會回傳 PagingData.empty()，而不會呼叫 UseCase
        verify(exactly = 0) { mockUseCase(any()) }
    }

    @Test
    fun `multiple rapid searches only trigger UseCase once due to debounce`() = runTest(testDispatcher) {
        // Arrange
        val query1 = "kotlin"
        val query2 = "android"
        
        val job = launch {
            viewModel.pagingDataFlow.collect { }
        }

        // Act
        viewModel.search(query1)
        advanceTimeBy(200) // 還沒到 500ms 就換下一個
        viewModel.search(query2)
        advanceTimeBy(600) // 這次等久一點

        // Assert
        verify(exactly = 0) { mockUseCase(query1) }
        verify(exactly = 1) { mockUseCase(query2) }
        
        job.cancel()
    }
}
