package com.example.searchbooks.feature

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String, val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Search : Screen("search")

    data object BookDetail : Screen(
        "detail/{bookId}",
        navArguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(bookId: String) = "detail/${bookId}"
    }
}
