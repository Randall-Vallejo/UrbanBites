package com.ucb.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.app.country.presentation.screen.CountryScreen
import com.ucb.app.crypto.presentation.screen.CryptoScreen
import com.ucb.app.demo.presentation.screen.DemoFuncionalidadesScreen
import com.ucb.app.fakestore.presentation.screen.StoreScreen
import com.ucb.app.firebase.presentation.screen.NotificationScreen
import com.ucb.app.github.presentation.screen.GitHubScreen
import com.ucb.app.login.presentation.screen.LoginScreen
import com.ucb.app.maps.presentation.screen.MapScreen
import com.ucb.app.movie.presentation.screen.MovieScreen
import kotlinx.coroutines.yield

@Composable
fun AppNavHost(
    destination: String? = null,
    onShowLocalNotification: () -> Unit = {},
    onRunWorker: () -> Unit = {},
    fcmToken: String = ""
) {
    val navController = rememberNavController()

    LaunchedEffect(destination) {
        if (!destination.isNullOrEmpty()) {
            yield()
            navController.navigate(NavRoute.Demo) {
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = NavRoute.Login) {
        composable<NavRoute.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoute.Demo) {
                        popUpTo(NavRoute.Login) { inclusive = true }
                    }
                }
            )
        }
        composable<NavRoute.Demo> {
            DemoFuncionalidadesScreen(
                onShowLocalNotification = onShowLocalNotification,
                onRunWorker = onRunWorker,
                fcmToken = fcmToken
            )
        }
        composable<NavRoute.Github> { GitHubScreen() }
        composable<NavRoute.Movies> { MovieScreen() }
        composable<NavRoute.Crypto> { CryptoScreen() }
        composable<NavRoute.FakeStore> { StoreScreen() }
        composable<NavRoute.CountryStore> { CountryScreen() }
        composable<NavRoute.Maps> { MapScreen() }
        composable<NavRoute.Notifications> { NotificationScreen() }
    }
}
