package com.ucb.app.navigation

import androidx.compose.runtime.*
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
import com.ucb.app.onboarding.presentation.screen.OnboardingScreen
import com.ucb.app.core.data.db.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.yield
import org.koin.compose.koinInject

@Composable
fun AppNavHost(
    destination: String? = null,
    onShowLocalNotification: () -> Unit = {},
    onRunWorker: () -> Unit = {},
    fcmToken: String = ""
) {
    val navController = rememberNavController()
    val database: AppDatabase = koinInject()
    var startRoute by remember { mutableStateOf<NavRoute?>(null) }

    LaunchedEffect(Unit) {
        val isCompleted = database.onboardingDao().isOnboardingCompleted().first() ?: false
        startRoute = if (isCompleted) NavRoute.Login else NavRoute.Onboarding
    }

    LaunchedEffect(destination) {
        if (!destination.isNullOrEmpty()) {
            yield()
            navController.navigate(NavRoute.Demo) {
                launchSingleTop = true
            }
        }
    }

    val currentStartRoute = startRoute
    if (currentStartRoute != null) {
        NavHost(navController = navController, startDestination = currentStartRoute) {
            composable<NavRoute.Onboarding> {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(NavRoute.Login) {
                            popUpTo(NavRoute.Onboarding) { inclusive = true }
                        }
                    }
                )
            }
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
                    fcmToken = fcmToken,
                    onNavigateToOnboarding = {
                        navController.navigate(NavRoute.Onboarding)
                    }
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
}
