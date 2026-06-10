package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.core.session.UserSession
import com.ucb.app.Res
import com.ucb.app.nav_profile
import com.ucb.app.profile_account
import com.ucb.app.profile_favorites
import com.ucb.app.profile_favorites_desc
import com.ucb.app.profile_language
import com.ucb.app.profile_language_desc
import com.ucb.app.profile_logout
import com.ucb.app.profile_logout_desc
import com.ucb.app.profile_made_in
import com.ucb.app.profile_my_business
import com.ucb.app.profile_notifications
import com.ucb.app.profile_notifications_desc
import com.ucb.app.profile_register_truck
import com.ucb.app.profile_register_truck_desc
import com.ucb.app.profile_settings
import com.ucb.app.profile_settings_desc
import com.ucb.app.profile_terms
import com.ucb.app.profile_terms_desc
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToLanguageChange: () -> Unit = {},
    onNavigateToAddFoodTruck: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onLogout: () -> Unit
) {
    val userName by UserSession.userName.collectAsState()
    val userEmail by UserSession.userEmail.collectAsState()
    
    val orangeColor = Color(0xFFFF5722)
    val redColor = Color(0xFFE64A19)

    Scaffold(
        bottomBar = {
            UrbanBitesBottomNav(
                orange = orangeColor,
                currentRoute = stringResource(Res.string.nav_profile),
                onHomeClick = onNavigateToHome,
                onMapClick = onNavigateToMap,
                onFavoritesClick = onNavigateToFavorites,
                onProfileClick = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(Brush.verticalGradient(listOf(orangeColor, redColor)))
                    .padding(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = userName,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userEmail,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu Options
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = stringResource(Res.string.profile_my_business),
                    style = MaterialTheme.typography.titleSmall,
                    color = orangeColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ProfileMenuItem(
                    icon = Icons.Default.Storefront, 
                    title = stringResource(Res.string.profile_register_truck), 
                    subtitle = stringResource(Res.string.profile_register_truck_desc), 
                    onClick = onNavigateToAddFoodTruck
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.profile_account),
                    style = MaterialTheme.typography.titleSmall,
                    color = orangeColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ProfileMenuItem(
                    icon = Icons.Default.Favorite, 
                    title = stringResource(Res.string.profile_favorites), 
                    subtitle = stringResource(Res.string.profile_favorites_desc), 
                    onClick = onNavigateToFavorites
                )
                ProfileMenuItem(
                    icon = Icons.Default.Notifications, 
                    title = stringResource(Res.string.profile_notifications), 
                    subtitle = stringResource(Res.string.profile_notifications_desc), 
                    onClick = onNavigateToNotifications
                )
                ProfileMenuItem(
                    icon = Icons.Default.Language, 
                    title = stringResource(Res.string.profile_language), 
                    subtitle = stringResource(Res.string.profile_language_desc), 
                    onClick = onNavigateToLanguage
                )
                ProfileMenuItem(
                    icon = Icons.Default.Settings, 
                    title = stringResource(Res.string.profile_settings), 
                    subtitle = stringResource(Res.string.profile_settings_desc), 
                    onClick = onNavigateToSettings
                )
                ProfileMenuItem(
                    icon = Icons.Default.Description, 
                    title = stringResource(Res.string.profile_terms), 
                    subtitle = stringResource(Res.string.profile_terms_desc), 
                    onClick = onNavigateToTerms
                )
                ProfileMenuItem(
                    icon = Icons.Default.Logout, 
                    title = stringResource(Res.string.profile_logout), 
                    subtitle = stringResource(Res.string.profile_logout_desc), 
                    isLast = true, 
                    onClick = onLogout
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Footer
            Text(
                text = "UrbanBites v1.0.0",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "© 2026 - " + stringResource(Res.string.profile_made_in),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isLast: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 56.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
