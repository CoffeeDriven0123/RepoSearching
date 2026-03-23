package com.example.reposearching.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.reposearching.R
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.data.model.RepoOwner
import com.example.reposearching.ui.theme.RepoSearchingTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * 顯示搜尋結果列表的 Composable（支援 Paging 3）。
 *
 * @param repos Paging 3 的資料來源。
 * @param isBookmarked 取得指定 repoId 的收藏狀態 Flow 的 factory 函式。
 * @param onBookmarkToggle 使用者點擊愛心按鈕時的回調，傳入 repo 與目前收藏狀態。
 * @param modifier 外部傳入的 [Modifier]。
 */
@Composable
fun RepoList(
    repos: LazyPagingItems<GithubRepo>,
    isBookmarked: (Long) -> Flow<Boolean>,
    onBookmarkToggle: (repo: GithubRepo, isCurrentlyBookmarked: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            count = repos.itemCount,
            key = repos.itemKey { it.htmlUrl },
            contentType = repos.itemContentType { "GithubRepo" }
        ) { index ->
            val repo = repos[index]
            if (repo != null) {
                val repoId = repo.htmlUrl.hashCode().toLong()
                val bookmarked by isBookmarked(repoId).collectAsState(initial = false)
                RepoItem(
                    repo = repo,
                    isBookmarked = bookmarked,
                    onBookmarkClick = { onBookmarkToggle(repo, bookmarked) }
                )
            }
        }
    }
}

/**
 * 單一 Repo 項目 Composable，包含收藏愛心按鈕。
 *
 * @param repo 欲顯示的 [GithubRepo] 資料。
 * @param isBookmarked 是否已收藏（控制愛心圖示的填實/空心狀態）。
 * @param onBookmarkClick 使用者點擊愛心按鈕時的回調。
 * @param modifier 外部傳入的 [Modifier]。
 */
@Composable
fun RepoItem(
    repo: GithubRepo,
    isBookmarked: Boolean = false,
    onBookmarkClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左側：擁有者頭像（使用 Coil 載入）
            AsyncImage(
                model = repo.owner.avatarUrl,
                contentDescription = stringResource(
                    R.string.content_description_owner_avatar,
                    repo.name
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 中間：垂直排列的 Repo 資訊
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.label_star_count, repo.stargazersCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = repo.htmlUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 右側：收藏愛心按鈕
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = if (isBookmarked) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = if (isBookmarked) {
                        stringResource(R.string.content_description_remove_bookmark)
                    } else {
                        stringResource(R.string.content_description_add_bookmark)
                    },
                    tint = if (isBookmarked) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = true, name = "RepoItem - 未收藏")
@Composable
private fun RepoItemNotBookmarkedPreview() {
    RepoSearchingTheme {
        RepoItem(
            repo = GithubRepo(
                name = "kotlinx.serialization",
                stargazersCount = 5432,
                htmlUrl = "https://github.com/Kotlin/kotlinx.serialization",
                owner = RepoOwner(avatarUrl = "")
            ),
            isBookmarked = false
        )
    }
}

@Preview(showBackground = true, name = "RepoItem - 已收藏")
@Composable
private fun RepoItemBookmarkedPreview() {
    RepoSearchingTheme {
        RepoItem(
            repo = GithubRepo(
                name = "kotlinx.serialization",
                stargazersCount = 5432,
                htmlUrl = "https://github.com/Kotlin/kotlinx.serialization",
                owner = RepoOwner(avatarUrl = "")
            ),
            isBookmarked = true
        )
    }
}
