package com.example.searchbooks.data.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.searchbooks.data.model.Book
import com.example.searchbooks.data.network.GoogleService
import com.example.searchbooks.data.paging.BooksPagingSource
import com.example.searchbooks.data.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepositoryImpl @Inject constructor(
    private val googleService: GoogleService,
) : BookRepository {

    override fun getSearchBooks(searchQuery: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                BooksPagingSource(
                    searchQuery = searchQuery,
                    service = googleService
                )
            }
        ).flow.map { pagingData ->
            pagingData.map {
                Book(
                    title = it.bookDetails.title,
                    authors = it.bookDetails.authors,
                    publisher = it.bookDetails.publisher,
                    smallImageUrl = it.bookDetails.imageLinks?.smallThumbnail
                )
            }
        }
    }
}