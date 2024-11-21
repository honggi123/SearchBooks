package com.example.searchbooks.data.model

data class Book(
    val id: String,
    val title: String,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val description: String? = null,
    val smallImageUrl: String? = null,
    val imageUrl: String? = null,
    val price: Double? = null,
    val retailPrice: Double? = null,
    val buyUrl: String? = null,
)
