package com.example.reposearching.di

import android.content.Context
import androidx.room.Room
import com.example.reposearching.data.local.AppDatabase
import com.example.reposearching.data.local.dao.BookmarkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module，負責提供 Room 資料庫及其 DAO 的實例。
 *
 * 安裝於 [SingletonComponent]，確保整個 App 生命週期內只有一個 [AppDatabase] 實例（Singleton），
 * 避免多個資料庫連線造成的資料不一致問題。
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * 提供 [AppDatabase] 的 Singleton 實例。
     *
     * 使用 [Room.databaseBuilder] 建立實體資料庫，並以 [AppDatabase.DATABASE_NAME] 作為名稱，
     * 避免 magic string。
     *
     * @param context Application Context，由 Hilt 透過 [@ApplicationContext] 自動注入。
     * @return App 唯一的 [AppDatabase] 實例。
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    /**
     * 提供 [BookmarkDao] 實例。
     *
     * 直接從 [AppDatabase] 取得，無需額外宣告 Singleton——
     * 因 [database] 本身已是 Singleton，每次呼叫 [AppDatabase.bookmarkDao] 都會回傳同一個 DAO。
     *
     * @param database 由 Hilt 自動注入的 [AppDatabase] 實例。
     * @return [BookmarkDao] 實例，供 Repository 層注入使用。
     */
    @Provides
    fun provideBookmarkDao(database: AppDatabase): BookmarkDao {
        return database.bookmarkDao()
    }
}
