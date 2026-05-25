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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.home.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.serialization.Serializable

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    
    val orangeColor = Color(0xFFFF5722)
    val redColor = Color(0xFFE64A19)
    val lightYellow = Color(0xFFFFF9C4)

    Scaffold(
        bottomBar = { UrbanBitesBottomNav(orangeColor) }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = orangeColor)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F8F8))
            ) {
                // Header Section
                item {
                    HeaderSection(uiState.userName, orangeColor, redColor)
                }

                // Categories Section
                item {
                    SectionTitle("Categorías")
                    CategoryList()
                }

                // Suggestions Section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionTitle("Sugerencias para ti", paddingValues = PaddingValues(0.dp))
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }

                items(uiState.suggestions) { truck ->
                    FoodTruckCard(truck, orangeColor, lightYellow)
                }

                // Near You Section
                item {
                    SectionTitle("Cerca de ti")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        items(uiState.foodTrucks) { truck ->
                            SmallFoodTruckCard(truck)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(userName: String, orange: Color, red: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(Brush.verticalGradient(listOf(orange, red)))
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hola, $userName 👋", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("¿Qué se te antoja hoy?", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Buscar food trucks, comidas...", color = Color.LightGray, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, paddingValues: PaddingValues = PaddingValues(16.dp)) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333),
        modifier = Modifier.padding(paddingValues)
    )
}

@Composable
fun CategoryList() {
    val categories = listOf("Hamburguesas", "Pizza", "Pollo", "Tacos", "Postres")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun FoodTruckCard(truck: FoodTruck, orange: Color, promoColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(Color.LightGray)) {
                Icon(Icons.Default.Restaurant, null, Modifier.align(Alignment.Center).size(50.dp), Color.White)
                
                Row(Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    if (truck.isPromo) {
                        Surface(color = Color.Red.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp)) {
                            Text("🔥 PROMO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                    if (truck.isOpen) {
                        Surface(color = Color(0xFF2E7D32).copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp)) {
                            Text("Abierto", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(truck.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(truck.category, fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.Favorite, null, tint = Color.Red)
                }

                if (truck.promoText.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Surface(
                        color = promoColor.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EmojiEvents, null, tint = orange, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(truck.promoText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                    Text(" ${truck.rating}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(" (${truck.reviews})", color = Color.Gray, fontSize = 12.sp)
                    Spacer(Modifier.width(16.dp))
                    Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Text(" ${truck.distance}", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SmallFoodTruckCard(truck: FoodTruck) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.LightGray)) {
                Icon(Icons.Default.Fastfood, null, Modifier.align(Alignment.Center), Color.White)
                Icon(
                    Icons.Default.FavoriteBorder, 
                    null, 
                    Modifier.align(Alignment.TopEnd).padding(8.dp).size(18.dp).background(Color.White.copy(0.5f), CircleShape).padding(2.dp),
                    Color.White
                )
            }
            Column(Modifier.padding(8.dp)) {
                Text(truck.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(truck.category, fontSize = 10.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
                    Text(" ${truck.rating}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text(truck.distance, fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun UrbanBitesBottomNav(orange: Color) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = orange
    ) {
        val items = listOf(
            Triple("Inicio", Icons.Default.Home, true),
            Triple("Mapa", Icons.Default.Map, false),
            Triple("Favoritos", Icons.Default.FavoriteBorder, false),
            Triple("Perfil", Icons.Default.Person, false)
        )

        items.forEach { (label, icon, selected) ->
            NavigationBarItem(
                selected = selected,
                onClick = {},
                icon = { Icon(icon, contentDescription = null) },
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

@Serializable
data class FoodTruck(
    val name: String = "",
    val category: String = "",
    val rating: String = "0.0",
    val reviews: String = "0",
    val distance: String = "0.0 km",
    val promoText: String = "",
    val isPromo: Boolean = false,
    val isOpen: Boolean = true
)
