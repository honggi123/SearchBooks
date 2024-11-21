package com.example.searchbooks.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchedBooksResponse(
    val totalItems: Int,
    val items: List<NetworkBook>,
)

@Serializable
data class NetworkBook(
    val id: String,
    @SerialName("volumeInfo") val bookDetails: NetworkBookDetails,
)
