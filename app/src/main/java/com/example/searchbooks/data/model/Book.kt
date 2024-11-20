package com.example.searchbooks.data.model

data class Book(
    val title: String,
    val authors: List<String>?,
    val publisher: String?,
    val smallImageUrl: String?
)
