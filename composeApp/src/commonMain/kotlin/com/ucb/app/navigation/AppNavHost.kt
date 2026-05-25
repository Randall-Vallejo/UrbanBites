package com.ucb.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ucb.app.demo.presentation.screen.DemoFuncionalidadesScreen
import com.ucb.app.home.presentation.screen.HomeScreen
import com.ucb.app.home.presentation.screen.FoodTruckDetailScreen
import com.ucb.app.home.presentation.screen.FavoritesScreen
import com.ucb.app.maps.presentation.screen.MapExploreScreen
import com.ucb.app.login.presentation.screen.LoginScreen
import com.ucb.app.login.presentation.screen.RegisterScreen
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
                    navController.navigate(NavRoute.Home) {
                        popUpTo(NavRoute.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavRoute.Register)
                }
            )
        }
        composable<NavRoute.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(NavRoute.Home) {
                        popUpTo(NavRoute.Login) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<NavRoute.Home> {
            HomeScreen(
                onTruckClick = { name ->
                    navController.navigate(NavRoute.Detail(name))
                },
                onNavigateToMap = {
                    navController.navigate(NavRoute.Map)
                },
                onNavigateToFavorites = {
                    navController.navigate(NavRoute.Favorites)
                }
            )
        }
        composable<NavRoute.Map> {
            MapExploreScreen(
                onBack = { navController.popBackStack() },
                onTruckClick = { name ->
                    navController.navigate(NavRoute.Detail(name))
                },
                onNavigateToFavorites = {
                    navController.navigate(NavRoute.Favorites)
                }
            )
        }
        composable<NavRoute.Favorites> {
            FavoritesScreen(
                onTruckClick = { name ->
                    navController.navigate(NavRoute.Detail(name))
                },
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMap = { navController.navigate(NavRoute.Map) }
            )
        }
        composable<NavRoute.Detail> { backStackEntry ->
            val detailRoute: NavRoute.Detail = backStackEntry.toRoute()
            FoodTruckDetailScreen(
                truckName = detailRoute.truckName,
                onBack = { navController.popBackStack() }
            )
        }
        composable<NavRoute.Demo> {
            DemoFuncionalidadesScreen(
                onShowLocalNotification = onShowLocalNotification,
                onRunWorker = onRunWorker,
                fcmToken = fcmToken
            )
        }
    }
}
