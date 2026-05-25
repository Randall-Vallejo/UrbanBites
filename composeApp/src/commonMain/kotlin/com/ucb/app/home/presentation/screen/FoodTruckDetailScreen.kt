package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.home.domain.model.FoodTruck
import com.ucb.app.home.domain.model.MenuDish
import com.ucb.app.home.domain.model.UserReview
import com.ucb.app.home.presentation.viewmodel.HomeViewModel
import com.ucb.app.maps.presentation.screen.MapScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FoodTruckDetailScreen(
    truckName: String,
    onBack: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val truck = uiState.foodTrucks.find { it.name == truckName }

    val orangeColor = Color(0xFFFF5722)

    if (truck == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (uiState.isLoading) CircularProgressIndicator(color = orangeColor)
            else Text("Local no encontrado")
        }
        return
    }

    Scaffold(
        bottomBar = { DetailBottomActions(orangeColor) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Header Image
            item {
                Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Color.LightGray)) {
                    Icon(Icons.Default.Restaurant, null, Modifier.align(Alignment.Center).size(60.dp), Color.White)
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        IconButton(onClick = onBack, modifier = Modifier.background(Color.White.copy(0.7f), CircleShape)) {
                            Icon(Icons.Default.ArrowBackIosNew, null, tint = Color.Black)
                        }
                        IconButton(onClick = {}, modifier = Modifier.background(Color.White.copy(0.7f), CircleShape)) {
                            Icon(Icons.Default.Favorite, null, tint = Color.Red)
                        }
                    }
                }
            }

            // Info Section
            item {
                Column(Modifier.padding(16.dp)) {
                    Text(truck.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(truck.description, color = Color.Gray, fontSize = 14.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
                        Text(" ${truck.rating}", fontWeight = FontWeight.Bold)
                        Text(" (${truck.reviewsCount} reseñas)", color = Color.Gray)
                    }
                }
            }

            // Menú REAL de Firebase
            if (truck.menu.isNotEmpty()) {
                item { Text("Menú destacado", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp)) }
                items(truck.menu) { item -> MenuItemRow(item) }
            }

            // Reseñas REALES de Firebase
            if (truck.userReviews.isNotEmpty()) {
                item { Text("Reseñas de clientes", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp)) }
                items(truck.userReviews) { review -> ReviewCard(review) }
            }

            // Ubicación con MAPA REAL
            item {
                Text("Ubicación", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))) {
                    MapScreen(
                        modifier = Modifier.fillMaxSize(),
                        centerLatitude = truck.latitude,
                        centerLongitude = truck.longitude,
                        trucks = listOf(truck)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, null, tint = Color(0xFFFF8A65), modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp)); Text(text, fontSize = 14.sp, color = Color.DarkGray)
    }
}

@Composable
fun MenuItemRow(item: MenuDish) {
    Surface(Modifier.padding(horizontal = 16.dp, vertical = 4.dp).fillMaxWidth(), color = Color.White, shape = RoundedCornerShape(12.dp), tonalElevation = 1.dp) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(item.name, fontWeight = FontWeight.Medium)
            Text("Bs. ${item.price}", color = Color(0xFFE64A19), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ReviewCard(review: UserReview) {
    Card(Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(review.userName, fontWeight = FontWeight.Bold)
                Row { repeat(review.stars) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp)) } }
            }
            Text(review.comment, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun DetailBottomActions(orange: Color) {
    Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = {}, modifier = Modifier.weight(1f).height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A47)), shape = RoundedCornerShape(12.dp)) {
            Icon(Icons.Default.NearMe, null); Spacer(Modifier.width(8.dp)); Text("Cómo llegar")
        }
        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF8A65))) {
            Icon(Icons.Default.Share, null, tint = Color(0xFFFF8A65)); Spacer(Modifier.width(8.dp)); Text("Compartir", color = Color(0xFFFF8A65))
        }
    }
}
