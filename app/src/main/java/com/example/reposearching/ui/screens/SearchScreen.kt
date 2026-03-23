package com.example.reposearching.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.reposearching.R
import com.example.reposearching.ui.components.RepoList
import com.example.reposearching.ui.viewmodel.SearchViewModel
import java.io.IOException

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val repos = viewModel.pagingDataFlow.collectAsLazyPagingItems()
    var text by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = text,
            onValueChange = { 
                text = it
                viewModel.search(it)
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text(stringResource(R.string.search_hint)) },
            singleLine = true
        )
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val refreshState = repos.loadState.refresh
            
            // [關鍵修改]：如果搜尋文字為空，不論狀態為何，都顯示提示文字而不是轉圈
            if (text.isBlank()) {
                Text(
                    text = stringResource(R.string.search_empty_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                when (refreshState) {
                    is LoadState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is LoadState.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val isNetworkError = refreshState.error is IOException
                            if (isNetworkError) {
                                Text(
                                    text = stringResource(R.string.search_network_error),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Button(onClick = { repos.retry() }) {
                                    Text(stringResource(R.string.search_retry))
                                }
                            } else {
                                Text(
                                    text = stringResource(R.string.search_general_error),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    is LoadState.NotLoading -> {
                        if (repos.itemCount == 0) {
                            Text(
                                text = stringResource(R.string.search_no_data),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            RepoList(
                                repos = repos,
                                isBookmarked = { repoId -> viewModel.isBookmarked(repoId) },
                                onBookmarkToggle = { repo, currentlyBookmarked ->
                                    viewModel.toggleBookmark(repo, currentlyBookmarked)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
