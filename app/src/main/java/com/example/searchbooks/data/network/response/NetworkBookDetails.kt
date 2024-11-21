package com.example.searchbooks.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class NetworkBookDetails(
    val title: String,
    val subtitle: String? = null,
    val authors: List<String> = emptyList(),
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val printedPageCount: Int? = null,
    val printType: String? = null,
    val categories: List<String> = emptyList(),
    val maturityRating: String? = null,
    val allowAnonLogging: Boolean? = null,
    val contentVersion: String? = null,
    val imageLinks: NetworkImageLinks? = null,
    val infoLink: String? = null,
    val canonicalVolumeLink: String? = null
)

@Serializable
data class NetworkImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null,
    val small: String? = null,
    val medium: String? = null,
    val large: String? = null,
    val extraLarge: String? = null
)