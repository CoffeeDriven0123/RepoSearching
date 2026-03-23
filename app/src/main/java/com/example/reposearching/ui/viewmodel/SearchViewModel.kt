package com.example.reposearching.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.domain.repository.BookmarkRepository
import com.example.reposearching.domain.usecase.SearchRepoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Search 頁面的 ViewModel。
 *
 * 負責：
 * 1. 管理搜尋關鍵字的 debounce 邏輯，驅動 [pagingDataFlow]。
 * 2. 代理「我的最愛」的新增 / 移除操作（透過 [BookmarkRepository]）。
 * 3. 提供 [isBookmarked] 方法，讓 UI 能訂閱特定 Repo 的收藏狀態 Flow。
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepoUseCase: SearchRepoUseCase,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 500L
    }

    private val _query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagingDataFlow: Flow<PagingData<GithubRepo>> = _query
        // 如果是空的（初始狀態），不延遲直接發送；否則才做 debounce
        .debounce { query ->
            if (query.isBlank()) 0L else SEARCH_DEBOUNCE_MS
        }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchRepoUseCase(query)
            }
        }
        .cachedIn(viewModelScope)

    /** 更新搜尋關鍵字 */
    fun search(query: String) {
        _query.value = query
    }

    /**
     * 切換某個 Repository 的收藏狀態。
     *
     * 若目前已收藏則移除，若未收藏則新增。
     * 使用 [isCurrentlyBookmarked] 作為當前狀態的參考，避免額外的非同步讀取。
     *
     * @param repo 被點擊的 [GithubRepo]。
     * @param isCurrentlyBookmarked 該 Repo 目前是否已被收藏（由 UI 從 [isBookmarked] Flow 取得）。
     */
    fun toggleBookmark(repo: GithubRepo, isCurrentlyBookmarked: Boolean) {
        viewModelScope.launch {
            val repoId = repo.htmlUrl.hashCode().toLong()
            if (isCurrentlyBookmarked) {
                bookmarkRepository.removeBookmark(repoId)
            } else {
                bookmarkRepository.addBookmark(repo)
            }
        }
    }

    /**
     * 取得特定 Repository 的收藏狀態 Flow。
     *
     * 每個 [RepoItem] 可藉由訂閱此 Flow 即時反映愛心圖示的開關狀態。
     *
     * @param repoId 透過 [GithubRepo.htmlUrl].hashCode().toLong() 計算的唯一 ID。
     * @return [Flow]，`true` 代表已收藏，`false` 代表未收藏。
     */
    fun isBookmarked(repoId: Long): Flow<Boolean> {
        return bookmarkRepository.isBookmarked(repoId)
    }
}
