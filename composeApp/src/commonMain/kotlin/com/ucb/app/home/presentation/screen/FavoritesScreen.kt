package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.home.presentation.viewmodel.HomeViewModel
import com.ucb.app.home.domain.model.FoodTruck
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    onTruckClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToMap: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val orangeColor = Color(0xFFFF5722)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // Adaptable
        bottomBar = {
            UrbanBitesBottomNav(
                orange = orangeColor,
                currentRoute = "Favoritos",
                onHomeClick = onNavigateBack,
                onMapClick = onNavigateToMap,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(
                    "Mis Favoritos", 
                    fontSize = 28.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "${favorites.size} food trucks guardados", 
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), 
                    fontSize = 14.sp
                )
            }

            if (favorites.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.FavoriteBorder, 
                            null, 
                            Modifier.size(64.dp), 
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text("Aún no tienes favoritos", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(favorites) { fav ->
                        FavoriteCard(fav) { onTruckClick(fav.name) }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(truck: com.ucb.app.home.data.db.entity.FavoriteTruckEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(Icons.Default.Restaurant, null, Modifier.align(Alignment.Center), MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(truck.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(truck.category, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 13.sp)
                
                Spacer(Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text(" ${truck.rating} ", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), modifier = Modifier.size(14.dp))
                    Text(truck.distance, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), fontSize = 12.sp)
                }

                if (truck.isOpen) {
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = Color(0xFF2E7D32).copy(alpha = 0.1f), 
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Abierto", 
                            color = Color(0xFF2E7D32), 
                            fontSize = 11.sp, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Icon(Icons.Default.Favorite, null, tint = Color.Red, modifier = Modifier.padding(8.dp))
        }
    }
}
