package com.example.searchbooks.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetBookDetailsResponse(
    val id: String,
    @SerialName("volumeInfo") val bookDetails: NetworkBookDetails,
    val saleInfo: SaleInfo,
)


@Serializable
data class SaleInfo(
    val country: String,
    val saleability: String,
    val isEbook: Boolean,
    val listPrice: Price? = null,
    val retailPrice: Price? = null,
    val buyLink: String? = null,
)

@Serializable
data class Price(
    val amount: Double,
    val currencyCode: String
)
