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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
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
            placeholder = { Text("搜尋 GitHub Repo") },
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
                    text = "請輸入關鍵字搜尋",
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
                                    text = "無網路連線",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Button(onClick = { repos.retry() }) {
                                    Text("重新載入")
                                }
                            } else {
                                Text(
                                    text = "查詢異常，請稍後再試",
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
                                text = "查無資料",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            RepoList(repos = repos)
                        }
                    }
                }
            }
        }
    }
}
