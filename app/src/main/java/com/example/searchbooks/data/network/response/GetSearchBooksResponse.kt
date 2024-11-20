package com.example.searchbooks.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchBooksResponse(
    val kind: String? = null,
    val totalItems: Int,
    val items: List<NetworkBook>,
)

@Serializable
data class NetworkBook(
    val id: String,
    @SerialName("volumeInfo") val bookDetails: BookDetails,
)

@Serializable
data class BookDetails(
    val title: String,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val imageLinks: ImageLinks? = null,
    val subtitle: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
)