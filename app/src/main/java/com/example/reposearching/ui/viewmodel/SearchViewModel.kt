package com.example.reposearching.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.reposearching.data.model.GithubRepo
import com.example.reposearching.domain.usecase.SearchRepoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepoUseCase: SearchRepoUseCase
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 500L
    }

    private val _query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val pagingDataFlow: Flow<PagingData<GithubRepo>> = _query
        // 如果是空的（初始狀態），不延遲直接發送；否則才做 debounce
        .debounce { query ->
            if (query.isBlank()) 0L else SEARCH_DEBOUNCE_MS
        }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchRepoUseCase(query)
            }
        }
        .cachedIn(viewModelScope)

    fun search(query: String) {
        _query.value = query
    }
}
