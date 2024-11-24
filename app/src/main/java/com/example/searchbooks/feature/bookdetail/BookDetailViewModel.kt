package com.example.searchbooks.feature.bookdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchbooks.data.exception.AppException
import com.example.searchbooks.data.model.Book
import com.example.searchbooks.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId =
        savedStateHandle.getStateFlow<String?>(BOOK_ID_SAVED_STATE_KEY, initialValue = null)

    val uiState: StateFlow<BookDetailUiState> =
        bookId
            .filterNotNull()
            .map<String, BookDetailUiState> { id ->
                val book = repository.getBookById(id)
                BookDetailUiState.Success(book)
            }.catch { exception ->
                emit(mapUiStateException(exception))
            }.stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                BookDetailUiState.Loading
            )

    private fun mapUiStateException(exception: Throwable): BookDetailUiState {
        return when (exception) {
            is IOException -> BookDetailUiState.Error(AppException.NetworkException())
            is HttpException -> BookDetailUiState.Error(AppException.ApiException())
            else -> BookDetailUiState.Error(AppException.UnknownException())
        }
    }

    companion object {
        private const val BOOK_ID_SAVED_STATE_KEY = "bookId"
    }
}

sealed class BookDetailUiState {
    data object Loading : BookDetailUiState()
    data class Success(val book: Book) : BookDetailUiState()
    data class Error(val exception: Exception) : BookDetailUiState()
}