package urbanbites.com.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import urbanbites.com.presentation.screen.ProfileEditScreen
import urbanbites.com.presentation.screen.ProfileScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoute.Profile
    ) {
        composable<NavRoute.Profile> {
            ProfileScreen(navController = navController)
        }

        composable<NavRoute.ProfileEdit> {
            ProfileEditScreen(navController = navController)
        }
    }
}
