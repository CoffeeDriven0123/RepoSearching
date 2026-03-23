package com.example.reposearching.data.repository

import android.util.Log
import com.example.reposearching.data.local.dao.BookmarkDao
import com.example.reposearching.data.local.entity.BookmarkEntity
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * [BookmarkRepository] 的具體實作。
 *
 * 封裝 [BookmarkDao]（Room）的操作，並依照 rules.md 規範，
 * 在每次資料庫操作前後記錄 Log，方便除錯。
 *
 * ## Mapping 策略
 * - **寫入**（[addBookmark]）：將 [GithubRepo] 轉換為 [BookmarkEntity] 後存入 Room。
 * - **讀取**（[getBookmarks]）：將 Room 回傳的 [BookmarkEntity] 清單對應回 [GithubRepo]，
 *   隔離 UI 層對資料庫實體的感知。
 */
class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    companion object {
        private const val TAG = "BookmarkRepository"
    }

    /**
     * 將 [GithubRepo] 加入我的最愛。
     *
     * 執行前後各記錄一筆 Log，方便追蹤資料庫寫入行為。
     */
    override suspend fun addBookmark(repo: GithubRepo) {
        Log.d(TAG, "addBookmark: 嘗試新增收藏 repoName=${repo.name}")
        val entity = repo.toBookmarkEntity()
        bookmarkDao.insertBookmark(entity)
        Log.d(TAG, "addBookmark: 收藏成功 repoName=${repo.name}")
    }

    /**
     * 從我的最愛中移除指定的 Repository。
     *
     * @param repoId 欲移除的 Repository GitHub ID。
     */
    override suspend fun removeBookmark(repoId: Long) {
        Log.d(TAG, "removeBookmark: 嘗試移除收藏 repoId=$repoId")
        bookmarkDao.deleteBookmarkById(repoId)
        Log.d(TAG, "removeBookmark: 移除成功 repoId=$repoId")
    }

    /**
     * 取得所有已收藏的 Repository 清單，以 [GithubRepo] 形式回傳。
     *
     * 使用 [Flow.map] 將 [BookmarkEntity] 清單對應為 [GithubRepo] 清單，
     * 讓 UI 層既不感知 Room Entity，也能即時收到資料庫的異動通知。
     */
    override fun getBookmarks(): Flow<List<GithubRepo>> {
        Log.d(TAG, "getBookmarks: 開始觀察我的最愛清單")
        return bookmarkDao.getAllBookmarks().map { entities ->
            entities.map { it.toGithubRepo() }
        }
    }

    /**
     * 查詢特定 Repository 是否已被收藏。
     *
     * @param repoId 欲查詢的 Repository GitHub ID。
     */
    override fun isBookmarked(repoId: Long): Flow<Boolean> {
        return bookmarkDao.isBookmarked(repoId)
    }

    // -------------------------------------------------------------------------
    // 私有 Mapping Extension Functions
    // -------------------------------------------------------------------------

    /**
     * 將 [GithubRepo] 轉換為可存入 Room 的 [BookmarkEntity]。
     *
     * 注意：目前 [GithubRepo] 沒有 `id` 欄位（GitHub API 的搜尋結果預設會回傳），
     * 若未來 API Model 有新增 `id`，請直接使用；
     * 此處暫時以 [GithubRepo.htmlUrl].hashCode() 取得穩定且唯一的 Long ID。
     */
    private fun GithubRepo.toBookmarkEntity(): BookmarkEntity {
        return BookmarkEntity(
            repoId = this.htmlUrl.hashCode().toLong(),
            name = this.name,
            ownerAvatarUrl = this.owner.avatarUrl,
            htmlUrl = this.htmlUrl,
            stargazersCount = this.stargazersCount
        )
    }

    /**
     * 將 Room 的 [BookmarkEntity] 還原為 Domain/UI 使用的 [GithubRepo]。
     */
    private fun BookmarkEntity.toGithubRepo(): GithubRepo {
        return GithubRepo(
            name = this.name,
            stargazersCount = this.stargazersCount,
            htmlUrl = this.htmlUrl,
            owner = com.example.reposearching.data.model.RepoOwner(
                avatarUrl = this.ownerAvatarUrl
            )
        )
    }
}
