package com.example.reposearching.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reposearching.R
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.ui.components.RepoItem
import com.example.reposearching.ui.theme.RepoSearchingTheme
import com.example.reposearching.ui.viewmodel.BookmarkViewModel

/**
 * 我的最愛頁面
 *
 * 顯示使用者收藏的所有 GitHub Repository。
 */
@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val bookmarks by viewModel.bookmarkedRepos.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.title_bookmarks),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.bookmark_empty_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = bookmarks,
                    key = { repo -> repo.htmlUrl }
                ) { repo ->
                    val repoId = repo.htmlUrl.hashCode().toLong()
                    RepoItem(
                        repo = repo,
                        isBookmarked = true, // 在我的最愛頁面裡，一定是被收藏的狀態
                        onBookmarkClick = {
                            viewModel.removeBookmark(repoId)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkScreenPreview() {
    RepoSearchingTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("預覽由於 Hilt ViewModel 依賴無法直接繪製，請參考 RepoItem 預覽")
        }
    }
}
