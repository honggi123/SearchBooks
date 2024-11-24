package com.example.searchbooks.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.searchbooks.data.model.Book
import com.example.searchbooks.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BookRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery =
        savedStateHandle.getStateFlow<String?>(key = SEARCH_QUERY, initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isNullOrEmpty()) {
                flowOf(SearchUiState.InputQuery)
            } else {
                val books = repository.getSearchBooks(query).cachedIn(viewModelScope)
                flowOf(
                    SearchUiState.Success(
                        books = books
                    )
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SearchUiState.Loading
        )

    fun onSearchTriggered(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }
}

sealed class SearchUiState {
    object InputQuery : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val books: Flow<PagingData<Book>>) : SearchUiState()
    data class Error(val exception: Exception) : SearchUiState()
}

private const val SEARCH_QUERY = "searchQuery"
