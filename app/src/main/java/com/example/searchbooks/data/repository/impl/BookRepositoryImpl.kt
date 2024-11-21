package com.example.searchbooks.data.repository.impl

import android.util.Log
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
                    id = it.id,
                    title = it.bookDetails.title,
                    authors = it.bookDetails.authors,
                    publisher = it.bookDetails.publisher,
                    smallImageUrl = it.bookDetails.imageLinks?.smallThumbnail
                )
            }
        }
    }

    override suspend fun getBookById(id: String): Book {
        val response = googleService.getBookDetailsById(id)
        Log.e("response", response.toString())

        Log.e("price", response.saleInfo.retailPrice.toString())
        Log.e("price", response.saleInfo.listPrice.toString())

        return Book(
            id = response.id,
            title = response.bookDetails.title,
            authors = response.bookDetails.authors,
            publisher = response.bookDetails.publisher,
            description = response.bookDetails.description,
            imageUrl = response.bookDetails.imageLinks?.thumbnail,
            smallImageUrl = response.bookDetails.imageLinks?.smallThumbnail,
            buyUrl = response.saleInfo.buyLink,
            price = response.saleInfo.listPrice?.amount,
            retailPrice = response.saleInfo.retailPrice?.amount
        )
    }
}