package com.example.searchbooks.exception

sealed class AppException(message: String? = null) : Exception(message) {
    class NetworkException(message: String? = null) : AppException(message)
    class ApiException(message: String? = null) : AppException(message)
    class UnknownException(message: String? = null) : AppException(message)
}