package com.ucb.app.maps.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.home.domain.model.FoodTruck
import com.ucb.app.home.presentation.screen.UrbanBitesBottomNav
import com.ucb.app.home.presentation.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.ucb.app.Res
import com.ucb.app.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MapExploreScreen(
    onBack: () -> Unit,
    onTruckClick: (String) -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val orangeColor = Color(0xFFFF5722)
    val darkTextColor = Color(0xFF333333)
    var showFilters by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            UrbanBitesBottomNav(
                orange = orangeColor,
                currentRoute = stringResource(Res.string.nav_map),
                onHomeClick = onBack,
                onFavoritesClick = onNavigateToFavorites,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                trucks = uiState.foodTrucks,
                onTruckClick = onTruckClick,
                onLocationResult = { lat, lon ->
                    viewModel.updateUserLocation(lat, lon)
                }
            )

            // BUSCADOR SUPERIOR
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, null, tint = Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            androidx.compose.foundation.text.BasicTextField(
                                value = searchQuery,
                                onValueChange = { 
                                    searchQuery = it
                                    viewModel.searchTrucks(it)
                                },
                                modifier = Modifier.weight(1f),
                                textStyle = androidx.compose.ui.text.TextStyle(color = darkTextColor),
                                decorationBox = { innerTextField ->
                                    if (searchQuery.isEmpty()) Text(stringResource(Res.string.common_search), color = Color.LightGray, fontSize = 14.sp)
                                    innerTextField()
                                }
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { showFilters = true },
                        modifier = Modifier.size(56.dp).background(Color.White, RoundedCornerShape(16.dp))
                    ) {
                        Icon(Icons.Default.FilterList, null, tint = orangeColor)
                    }
                }
            }

            // TARJETA INFERIOR ( Food trucks cercanos )
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth().heightIn(max = 280.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 10.dp
            ) {
                Column(Modifier.padding(16.dp)) {
                    Box(Modifier.width(40.dp).height(4.dp).background(Color.LightGray, CircleShape).align(Alignment.CenterHorizontally))
                    Spacer(Modifier.height(16.dp))
                    
                    Text(stringResource(Res.string.map_nearby_trucks), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = darkTextColor)
                    
                    Spacer(Modifier.height(12.dp))
                    
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.foodTrucks) { truck ->
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable { onTruckClick(truck.name) }.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(Modifier.size(50.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF5F5F5))) {
                                    Icon(Icons.Default.Fastfood, null, Modifier.align(Alignment.Center).size(24.dp), Color.Gray)
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(truck.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = darkTextColor)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                                        Text(" ${truck.rating} • ${truck.distance}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                Icon(Icons.Default.ArrowForwardIos, null, Modifier.size(14.dp), Color.LightGray)
                            }
                        }
                    }
                }
            }

            if (showFilters) {
                AlertDialog(
                    onDismissRequest = { showFilters = false },
                    confirmButton = { TextButton(onClick = { showFilters = false }) { Text(stringResource(Res.string.common_close)) } },
                    title = { Text(stringResource(Res.string.map_filter_title), fontWeight = FontWeight.Bold, color = darkTextColor) },
                    containerColor = Color.White,
                    text = {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val categories = listOf("Todos", "Hamburguesas", "Pizza", "Pollo", "Tacos")
                            categories.forEach { cat ->
                                SuggestionChip(
                                    onClick = { 
                                        viewModel.filterByCategory(if (cat == "Todos") null else cat)
                                        showFilters = false
                                    },
                                    label = { Text(cat) }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
