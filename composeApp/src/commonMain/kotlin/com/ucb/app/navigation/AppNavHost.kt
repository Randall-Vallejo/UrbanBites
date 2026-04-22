package com.ucb.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.app.cart.presentation.screen.CartScreen
import com.ucb.app.cart.presentation.viewmodel.CartViewModel
import com.ucb.app.country.presentation.screen.CountryScreen
import com.ucb.app.crypto.presentation.screen.CryptoScreen
import com.ucb.app.fakestore.presentation.screen.StoreScreen
import com.ucb.app.firebase.presentation.screen.NotificationScreen
import com.ucb.app.github.presentation.screen.GitHubScreen
import com.ucb.app.maps.presentation.screen.MapScreen
import com.ucb.app.movie.presentation.screen.MovieScreen
import kotlinx.coroutines.yield
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(destination: String? = null) {
    val navController = rememberNavController()

    LaunchedEffect(destination) {
        if (!destination.isNullOrEmpty()) {
            yield()
            navController.navigate(NavRoute.Notifications) {
                launchSingleTop = true
            }
        }
    }

    // Cambiado startDestination de NavRoute.Cart a NavRoute.Notifications para tus pruebas
    NavHost(navController = navController, startDestination = NavRoute.Notifications) {
        composable<NavRoute.Github> {
            GitHubScreen()
        }
        composable<NavRoute.Movies> {
            MovieScreen()
        }
        composable<NavRoute.Crypto> {
            CryptoScreen()
        }
        composable<NavRoute.FakeStore> {
            StoreScreen()
        }
        composable<NavRoute.CountryStore> {
            CountryScreen()
        }
        composable<NavRoute.Maps> {
            MapScreen()
        }
        composable<NavRoute.Notifications> {
            NotificationScreen()
        }
        composable<NavRoute.Cart> {
            val cartViewModel = koinViewModel<CartViewModel>()
            CartScreen(viewModel = cartViewModel)
        }
    }
}
