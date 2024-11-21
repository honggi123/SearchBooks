package com.example.searchbooks.data.repository

import androidx.paging.PagingData
import com.example.searchbooks.data.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getSearchBooks(searchQuery: String): Flow<PagingData<Book>>

    suspend fun getBookById(id: String): Book

}