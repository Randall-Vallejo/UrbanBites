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
    // Buscamos el truck real en el estado que viene de Firebase
    val truck = uiState.foodTrucks.find { it.name == truckName }

    val orangeColor = Color(0xFFFF5722)
    val redColor = Color(0xFFE64A19)

    if (truck == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = orangeColor)
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No se encontró información del local")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onBack) { Text("Volver") }
                }
            }
        }
        return
    }

    Scaffold(
        bottomBar = {
            DetailBottomActions(orangeColor)
        }
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
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.background(Color.White.copy(0.7f), CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = Color.Black)
                        }
                        IconButton(
                            onClick = { },
                            modifier = Modifier.background(Color.White.copy(0.7f), CircleShape)
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = "Favorito", tint = Color.Red)
                        }
                    }

                    if (truck.isOpen) {
                        Surface(
                            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                            color = Color(0xFF2E7D32),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(8.dp).background(Color.White, CircleShape))
                                Spacer(Modifier.width(8.dp))
                                Text("Abierto ahora", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
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
                        Spacer(Modifier.width(12.dp))
                        Surface(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp)) {
                            Text(truck.category, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    InfoRow(Icons.Default.LocationOn, "Cochabamba, Bolivia")
                    InfoRow(Icons.Default.AccessTime, "10:00 AM - 10:00 PM")
                    InfoRow(Icons.Default.NearMe, "${truck.distance} de distancia")
                }
            }

            // Promo Section
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    if (truck.isPromo) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(Modifier.background(Brush.horizontalGradient(listOf(Color(0xFFFF5252), Color(0xFFFF8A65)))).padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.TrendingUp, null, tint = Color.White)
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text("¡Promoción Especial!", color = Color.White, fontWeight = FontWeight.Bold)
                                        Text(truck.promoText, color = Color.White.copy(0.9f), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFFF9C4),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, null, tint = orangeColor)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Combo Destacado", fontWeight = FontWeight.Bold)
                                Text("Revisa los combos del día en el local", fontSize = 12.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }

            // Gallery Section
            item {
                Text("Galería", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(3) {
                        Box(Modifier.size(150.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFEEEEEE))) {
                            Icon(Icons.Default.Image, null, Modifier.align(Alignment.Center), Color.Gray)
                        }
                    }
                }
            }

            // Menu Section
            if (truck.menu.isNotEmpty()) {
                item {
                    Text("Menú destacado", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                }
                items(truck.menu) { item ->
                    MenuItemRow(item)
                }
            }

            // Reviews Section
            if (truck.userReviews.isNotEmpty()) {
                item {
                    Text("Reseñas de clientes", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                }
                items(truck.userReviews) { review ->
                    ReviewCard(review)
                }
            }

            // Location Section with REAL MAP API
            item {
                Text("Ubicación", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    MapScreen(
                        modifier = Modifier.fillMaxSize(),
                        latitude = truck.latitude,
                        longitude = truck.longitude,
                        title = truck.name
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
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = Color.DarkGray)
    }
}

@Composable
fun MenuItemRow(item: MenuDish) {
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp).fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(item.name, fontWeight = FontWeight.Medium)
            Text("Bs. ${item.price}", color = Color(0xFFE64A19), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ReviewCard(review: UserReview) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(review.userName, fontWeight = FontWeight.Bold)
                Row {
                    repeat(review.stars) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    }
                }
            }
            Text(review.comment, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun DetailBottomActions(orange: Color) {
    Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = {},
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A47)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.NearMe, null)
            Spacer(Modifier.width(8.dp))
            Text("Cómo llegar")
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF8A65))
        ) {
            Icon(Icons.Default.Share, null, tint = Color(0xFFFF8A65))
            Spacer(Modifier.width(8.dp))
            Text("Compartir", color = Color(0xFFFF8A65))
        }
    }
}
