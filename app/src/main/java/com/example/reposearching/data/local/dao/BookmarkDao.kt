package com.example.reposearching.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.reposearching.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO（Data Access Object）介面，定義對 `bookmarks` 資料表的所有操作。
 *
 * ## 設計原則
 * - **新增/刪除** 使用 `suspend` 函式，強制在協程（Coroutine）中呼叫，符合 rules.md 規範。
 * - **查詢** 回傳 `Flow`，Room 會在資料表發生變化時自動發射新值，讓 ViewModel 的 UI 狀態
 *   能即時響應，無需手動刷新。
 *
 * ## Logging Hook
 * 此 DAO 不直接負責 Logging。依據 rules.md 規範（「所有網路與資料庫操作必須記錄 Log」），
 * Logging 應在呼叫端（Repository）中於調用 DAO 方法前後寫入，
 * 以保持 DAO 的單一職責（只做資料庫 CRUD）。
 */
@Dao
interface BookmarkDao {

    /**
     * 將一筆 Repository 加入我的最愛。
     *
     * 使用 [OnConflictStrategy.REPLACE] 策略：若該 [BookmarkEntity.repoId] 已存在，
     * 則以新資料覆蓋（例如更新星星數），防止重複收藏錯誤。
     *
     * @param bookmark 欲收藏的 [BookmarkEntity] 資料。
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    /**
     * 依 repoId 從我的最愛中刪除一筆 Repository。
     *
     * 使用具名參數 `:repoId` 以提高 SQL 的可讀性，明確對應欄位名稱 `repo_id`。
     *
     * @param repoId 欲移除的 Repository 之 GitHub ID。
     */
    @Query("DELETE FROM bookmarks WHERE repo_id = :repoId")
    suspend fun deleteBookmarkById(repoId: Long)

    /**
     * 取得所有已收藏的 Repository 清單，依收藏時間降序排列（最新收藏的排在前面）。
     *
     * 回傳 [Flow]，當 `bookmarks` 資料表有任何異動時，Room 會自動發射最新的完整清單，
     * 供 BookmarkViewModel 轉換為 UI State。
     *
     * @return [Flow]，每次資料庫更新時都會發射最新的 [BookmarkEntity] 清單。
     */
    @Query("SELECT * FROM bookmarks ORDER BY bookmarked_at DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    /**
     * 查詢特定 repoId 是否已被收藏，供搜尋列表的愛心圖示即時顯示正確狀態。
     *
     * 回傳 [Flow<Boolean>]，當收藏狀態改變時（例如：使用者新增或移除收藏），
     * 此 Flow 會自動更新，驅動搜尋列表 UI 中的愛心圖示切換。
     *
     * ## 查詢說明
     * `SELECT COUNT(*) > 0`：若找到符合的 `repo_id`，回傳 `true`（已收藏）；
     * 反之回傳 `false`（未收藏）。
     *
     * @param repoId 欲查詢的 Repository 之 GitHub ID。
     * @return [Flow<Boolean>]，`true` 代表已收藏，`false` 代表未收藏。
     */
    @Query("SELECT COUNT(*) > 0 FROM bookmarks WHERE repo_id = :repoId")
    fun isBookmarked(repoId: Long): Flow<Boolean>
}
