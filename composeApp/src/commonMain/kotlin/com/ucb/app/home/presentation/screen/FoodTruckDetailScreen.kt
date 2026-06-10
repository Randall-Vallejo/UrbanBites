package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.core.util.toOneDecimal
import com.ucb.app.home.domain.model.MenuDish
import com.ucb.app.home.domain.model.UserReview
import com.ucb.app.home.presentation.viewmodel.HomeViewModel
import com.ucb.app.maps.presentation.screen.MapScreen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FoodTruckDetailScreen(
    truckName: String,
    onBack: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    
    val truck = uiState.foodTrucks.find { it.name == truckName }
    val isFavorite = favorites.any { it.name == truckName }

    val orangeColor = Color(0xFFFF5722)

    // Lógica de Estado Abierto/Cerrado (Requerimiento 3 - TIEMPO REAL)
    val isOpenRealTime = remember(truck) {
        if (truck == null || truck.openingTime.isBlank() || truck.closingTime.isBlank()) true
        else {
            try {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                val currentTimeMinutes = now.hour * 60 + now.minute
                
                val openParts = truck.openingTime.split(":")
                val closeParts = truck.closingTime.split(":")
                val openTimeMinutes = openParts[0].toInt() * 60 + openParts[1].toInt()
                val closeTimeMinutes = closeParts[0].toInt() * 60 + closeParts[1].toInt()
                
                if (closeTimeMinutes < openTimeMinutes) { // Horario nocturno (ej: 18:00 - 02:00)
                    currentTimeMinutes >= openTimeMinutes || currentTimeMinutes <= closeTimeMinutes
                } else {
                    currentTimeMinutes in openTimeMinutes..closeTimeMinutes
                }
            } catch (e: Exception) { true }
        }
    }

    if (truck == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = orangeColor)
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Cargando información del local...")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onBack) { Text("Volver") }
                }
            }
        }
        return
    }

    // Calcular Rating Real dinámicamente (Requerimiento 2)
    val averageRating = remember(truck.userReviews) {
        if (truck.userReviews.isEmpty()) truck.rating.toDoubleOrNull() ?: 0.0
        else truck.userReviews.map { it.stars }.average()
    }

    Scaffold(
        bottomBar = {
            DetailBottomActions(
                orange = orangeColor,
                lat = truck.latitude,
                lng = truck.longitude,
                name = truck.name
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(250.dp).background(Color.LightGray)) {
                    if (truck.imageUrl.isNotBlank()) {
                        KamelImage(
                            resource = asyncPainterResource(truck.imageUrl),
                            contentDescription = truck.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onLoading = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = orangeColor, strokeWidth = 2.dp) } },
                            onFailure = { Icon(Icons.Default.Restaurant, null, Modifier.align(Alignment.Center).size(60.dp), Color.White) }
                        )
                    } else {
                        Icon(Icons.Default.Restaurant, null, Modifier.align(Alignment.Center).size(60.dp), Color.White)
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.background(Color.White.copy(0.7f), CircleShape)) {
                            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = Color.Black)
                        }
                        IconButton(onClick = { viewModel.toggleFavorite(truck) }, modifier = Modifier.background(Color.White.copy(0.7f), CircleShape)) {
                            Icon(imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, tint = if (isFavorite) Color.Red else Color.Gray, contentDescription = null)
                        }
                    }

                    // Indicador Dinámico de Estado (Requerimiento 3)
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                        color = if (isOpenRealTime) Color(0xFF2E7D32) else Color.Red,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(8.dp).background(Color.White, CircleShape))
                            Spacer(Modifier.width(8.dp))
                            Text(if (isOpenRealTime) "Abierto ahora" else "Cerrado", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Column(Modifier.padding(16.dp)) {
                    Text(truck.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text(truck.description, color = Color.Gray, fontSize = 14.sp)
                    Spacer(Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(20.dp))
                        Text(" ${averageRating.toOneDecimal()}", fontWeight = FontWeight.Bold)
                        Text(" (${truck.userReviews.size} reseñas)", color = Color.Gray)
                        Spacer(Modifier.width(12.dp))
                        Surface(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp)) {
                            Text(truck.category, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    InfoRow(Icons.Default.LocationOn, "Cochabamba, Bolivia")
                    InfoRow(Icons.Default.AccessTime, "${truck.openingTime} - ${truck.closingTime}")
                    InfoRow(Icons.Default.NearMe, "${truck.distance} de distancia")
                }
            }

            // Sección de Reseñas Reales e Interactivas (Requerimiento 3)
            item {
                ReviewInputSection(orangeColor) { stars, comment ->
                    viewModel.addReview(truck.id, stars, comment)
                }
            }

            if (truck.userReviews.isNotEmpty()) {
                item { Text("Opiniones reales de la comunidad", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp)) }
                items(truck.userReviews.reversed()) { review -> ReviewCard(review) }
            }

            item {
                Text("Ubicación exacta", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))) {
                    MapScreen(modifier = Modifier.fillMaxSize(), centerLatitude = truck.latitude, centerLongitude = truck.longitude, trucks = listOf(truck))
                }
            }
        }
    }
}

@Composable
fun ReviewInputSection(color: Color, onSendReview: (Int, String) -> Unit) {
    var stars by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    
    Card(modifier = Modifier.padding(16.dp).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))) {
        Column(Modifier.padding(16.dp)) {
            Text("¡Danos tu opinión!", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(Modifier.padding(vertical = 8.dp)) {
                repeat(5) { index ->
                    IconButton(onClick = { stars = index + 1 }, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Star, null, tint = if (index < stars) Color(0xFFFFB300) else Color.LightGray)
                    }
                }
            }
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Escribe tu experiencia aquí...") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp)
            )
            Button(
                onClick = { 
                    if (comment.isNotBlank()) {
                        onSendReview(stars, comment)
                        comment = ""
                        stars = 5
                    }
                },
                modifier = Modifier.align(Alignment.End).padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {
                Text("Publicar reseña")
            }
        }
    }
}

@Composable
expect fun DetailBottomActions(orange: Color, lat: Double, lng: Double, name: String)

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, null, tint = Color(0xFFFF8A65), modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = Color.DarkGray)
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
                Text(review.userName.ifBlank { "Anónimo" }, fontWeight = FontWeight.Bold)
                Row { repeat(review.stars) { Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp)) } }
            }
            Text(review.comment, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
