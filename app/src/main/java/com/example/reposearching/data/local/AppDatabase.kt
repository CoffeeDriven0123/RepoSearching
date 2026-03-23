package com.example.reposearching.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reposearching.data.local.dao.BookmarkDao
import com.example.reposearching.data.local.entity.BookmarkEntity

/**
 * 應用程式的 Room 資料庫。
 *
 * 目前包含的資料表：
 * - [BookmarkEntity]：使用者「我的最愛」的 Repository 資料。
 *
 * 當需要新增資料表時，請在 [entities] 陣列中增加對應 Entity，
 * 並記得遞增 [version] 並提供 Migration 策略。
 */
@Database(
    entities = [BookmarkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /** 取得操作「我的最愛」的 DAO */
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        /** 資料庫名稱常數，供 Hilt Module 建立資料庫時使用 */
        const val DATABASE_NAME = "repo_searching_db"
    }
}
