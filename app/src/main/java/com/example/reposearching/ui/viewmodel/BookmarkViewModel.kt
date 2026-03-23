package com.example.reposearching.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.domain.repository.BookmarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 「我的最愛」頁面的 ViewModel。
 *
 * 負責：
 * 1. 取得所有收藏的 Repository，轉換為 [StateFlow] 讓 BookmarkScreen 觀察。
 * 2. 處理移除收藏的動作。
 */
@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    /**
     * 觀察所有已被收藏的 Repository。
     * 透過 [stateIn] 將 Flow 轉換為 StateFlow，
     * 並指定在有訂閱者時開始啟動（[SharingStarted.WhileSubscribed]）。
     */
    val bookmarkedRepos: StateFlow<List<GithubRepo>> = bookmarkRepository.getBookmarks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * 移除特定 GitHub Repo 的收藏狀態。
     *
     * @param repoId 需移除的 GitHub Repo 唯一 ID
     */
    fun removeBookmark(repoId: Long) {
        viewModelScope.launch {
            bookmarkRepository.removeBookmark(repoId)
        }
    }
}
