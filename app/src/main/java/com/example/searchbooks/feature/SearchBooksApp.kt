package com.example.searchbooks.feature

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.searchbooks.feature.bookdetail.BookDetailScreen
import com.example.searchbooks.feature.search.SearchScreen


@Composable
fun SearchBooksApp() {
    val navController = rememberNavController()
    SunFlowerNavHost(
        navController = navController
    )
}

@Composable
fun SunFlowerNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screen.Search.route) {
        composable(route = Screen.Search.route) {
            SearchScreen(
                onBookClick = {
                    navController.navigate(
                        Screen.BookDetail.createRoute(
                            bookId = it.id
                        )
                    )
                }
            )
        }
        composable(
            route = Screen.BookDetail.route,
            arguments = Screen.BookDetail.navArguments
        ) {
            BookDetailScreen(
                onBackClick = { navController.navigateUp() },
            )
        }
    }
}
