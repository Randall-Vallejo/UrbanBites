package com.ucb.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.app.github.presentation.screen.GitHubScreen
import com.ucb.app.movie.presentation.screen.MovieScreen
import com.ucb.app.presentation.screen.ProfileEditScreen
import com.ucb.app.presentation.screen.ProfileScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoute.Movie
    ) {
        composable<NavRoute.Profile> {
            ProfileScreen(navController = navController)
        }

        composable<NavRoute.ProfileEdit> {
            ProfileEditScreen(navController = navController)
        }

        composable<NavRoute.GitHub> {
            GitHubScreen()
        }

        composable<NavRoute.Movie> {
            MovieScreen()
        }
    }
}
