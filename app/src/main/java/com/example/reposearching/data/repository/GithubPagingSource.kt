package com.example.reposearching.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.data.remote.GithubApi
import retrofit2.HttpException
import java.io.IOException

class GithubPagingSource(
    private val api: GithubApi,
    private val query: String
) : PagingSource<Int, GithubRepo>() {

    companion object {
        private const val TAG = "GithubPagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> {
        val page = params.key ?: 1 // 如果是初始加載，從第 1 頁開始
        
        Log.d(TAG, "開始載入分頁: $page, 載入數量: ${params.loadSize}, 關鍵字: '$query'")

        return try {
            val response = api.searchRepositories(
                query = query,
                page = page,
                perPage = params.loadSize
            )

            val repos = response.items
            Log.d(TAG, "分頁 $page 載入成功, 取得 ${repos.size} 筆資料")
            
            LoadResult.Page(
                data = repos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (repos.isEmpty()) null else page + (params.loadSize / 30).coerceAtLeast(1)
            )
        } catch (e: IOException) {
            Log.e(TAG, "分頁 $page 載入失敗: 網路錯誤", e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "分頁 $page 載入失敗: HTTP 錯誤 (${e.code()})", e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "分頁 $page 載入時發生非預期錯誤", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPos)?.nextKey?.minus(1)
        }
    }
}
