package com.example.reposearching.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.reposearching.data.model.GithubRepo

import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

/**
 * 顯示搜尋結果列表的 Compose 元件 (支援 Paging 3)
 */
@Composable
fun RepoList(
    repos: LazyPagingItems<GithubRepo>,
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
                RepoItem(repo = repo)
            }
        }
    }
}

/**
 * 單一的 Repo 項目 Compose 元件
 */
@Composable
fun RepoItem(
    repo: GithubRepo,
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
            // 左側：擁有者頭像 (使用 Coil 載入)
            AsyncImage(
                model = repo.owner.avatarUrl,
                contentDescription = "${repo.name} owner avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    // 如果希望是圓角，可以加上 .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 右側：垂直排列的資訊
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Repo 名稱
                Text(
                    text = repo.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 星數
                Text(
                    text = "⭐️ ${repo.stargazersCount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 網址
                Text(
                    text = repo.htmlUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
