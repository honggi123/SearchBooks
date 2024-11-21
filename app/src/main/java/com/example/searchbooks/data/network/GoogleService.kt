package com.example.searchbooks.data.network

import com.example.searchbooks.data.network.response.GetBookDetailsResponse
import com.example.searchbooks.data.network.response.GetSearchedBooksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleService {

    @GET("books/v1/volumes")
    suspend fun getBooksByQuery(
        @Query("q") searchQuery: String,
        @Query("startIndex") start: Int,
        @Query("fields") fields: String = BASIC_BOOK_FIELDS,
    ): GetSearchedBooksResponse

    @GET("books/v1/volumes/{id}")
    suspend fun getBookDetailsById(
        @Path("id") id: String,
    ): GetBookDetailsResponse

    companion object {
        const val BASIC_BOOK_FIELDS =
            "totalItems,items(id,volumeInfo(title,authors,publisher,imageLinks))"
        const val FULL_BOOK_FIELDS =
            "items(id,volumeInfo(title,authors,publisher,imageLinks))"
    }
}

