package com.example.reposearching.domain.repository

import com.example.reposearching.data.model.GithubRepo
import kotlinx.coroutines.flow.Flow

/**
 * 「我的最愛」功能的 Domain 層 Repository 介面。
 *
 * 定義對「收藏」資料的業務操作，隔離 UI 層與資料來源（Room）。
 * 所有實作細節（資料庫操作、Logging）均由 [com.example.reposearching.data.repository.BookmarkRepositoryImpl] 負責。
 */
interface BookmarkRepository {

    /**
     * 將一個 Repository 加入我的最愛。
     *
     * @param repo 欲收藏的 [GithubRepo] 物件。
     */
    suspend fun addBookmark(repo: GithubRepo)

    /**
     * 將一個 Repository 從我的最愛中移除。
     *
     * @param repoId 欲移除的 Repository 之 GitHub ID。
     */
    suspend fun removeBookmark(repoId: Long)

    /**
     * 取得所有已收藏的 Repository 清單（以 [GithubRepo] 形式回傳）。
     *
     * 回傳 [Flow]，當收藏清單發生任何變化時會自動發射最新資料。
     *
     * @return [Flow] 包含所有已收藏的 [GithubRepo] 清單。
     */
    fun getBookmarks(): Flow<List<GithubRepo>>

    /**
     * 查詢特定 Repository 是否已被加入我的最愛。
     *
     * 回傳 [Flow]，當該 Repository 的收藏狀態改變時會自動更新，
     * 用於驅動搜尋列表中愛心圖示的即時顯示。
     *
     * @param repoId 欲查詢的 Repository 之 GitHub ID。
     * @return [Flow]，`true` 代表已收藏，`false` 代表未收藏。
     */
    fun isBookmarked(repoId: Long): Flow<Boolean>
}
