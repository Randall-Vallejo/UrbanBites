package com.ucb.app.home.presentation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ucb.app.Res
import com.ucb.app.nav_home
import com.ucb.app.nav_map
import com.ucb.app.nav_favorites
import com.ucb.app.nav_profile
import org.jetbrains.compose.resources.stringResource

@Composable
fun UrbanBitesBottomNav(
    orange: Color, 
    currentRoute: String,
    onHomeClick: () -> Unit = {},
    onMapClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = orange
    ) {
        val items = listOf(
            Triple(stringResource(Res.string.nav_home), Icons.Default.Home, onHomeClick),
            Triple(stringResource(Res.string.nav_map), Icons.Default.Map, onMapClick),
            Triple(stringResource(Res.string.nav_favorites), Icons.Default.FavoriteBorder, onFavoritesClick),
            Triple(stringResource(Res.string.nav_profile), Icons.Default.Person, onProfileClick)
        )
        items.forEach { (label, icon, onClick) ->
            NavigationBarItem(
                selected = label == currentRoute, 
                onClick = onClick, 
                icon = { Icon(icon, null) },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = orange,
                    selectedTextColor = orange,
                    indicatorColor = orange.copy(alpha = 0.1f),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
