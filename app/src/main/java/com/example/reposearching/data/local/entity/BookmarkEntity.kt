package com.example.reposearching.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity，代表「我的最愛」的資料表結構。
 *
 * 每一筆記錄對應一個使用者收藏的 GitHub Repository。
 * 僅儲存顯示清單所需的必要欄位，避免儲存不必要的資料。
 *
 * @param repoId GitHub 上 Repository 的唯一 ID，用作 Primary Key，防止重複收藏。
 * @param name Repository 名稱，例如 "kotlinx.serialization"。
 * @param ownerAvatarUrl 擁有者頭像的圖片 URL。
 * @param htmlUrl Repository 在 GitHub 上的網頁連結。
 * @param stargazersCount 星星數（收藏當下記錄的值）。
 * @param bookmarkedAt 使用者加入收藏時的 timestamp（毫秒），用於排序清單（最新收藏的顯示在前）。
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    @ColumnInfo(name = "repo_id")
    val repoId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "owner_avatar_url")
    val ownerAvatarUrl: String,

    @ColumnInfo(name = "html_url")
    val htmlUrl: String,

    @ColumnInfo(name = "stargazers_count")
    val stargazersCount: Int,

    @ColumnInfo(name = "bookmarked_at")
    val bookmarkedAt: Long = System.currentTimeMillis()
)
