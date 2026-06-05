package com.ucb.app.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.home.presentation.state.HomeUiState
import com.ucb.app.home.domain.model.FoodTruck
import com.ucb.app.home.domain.model.MenuDish
import com.ucb.app.home.domain.model.UserReview
import com.ucb.app.home.data.db.dao.FavoriteDao
import com.ucb.app.home.data.db.entity.FavoriteTruckEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.*

class HomeViewModel(
    private val firebaseManager: FirebaseManager,
    private val favoriteDao: FavoriteDao
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private var allTrucks: List<FoodTruck> = emptyList()
    private var selectedCategory: String? = null
    private var searchQuery: String = ""
    private var userLat: Double? = null
    private var userLon: Double? = null

    private val json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
    }

    val favorites = favoriteDao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeFoodTrucks()
    }

    private fun observeFoodTrucks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            firebaseManager.observeData("food_trucks_v3").collect { jsonData ->
                if (jsonData != null && jsonData != "null") {
                    try {
                        val trucks = json.decodeFromString<List<FoodTruck>>(jsonData)
                        allTrucks = trucks
                        applyFilters()
                    } catch (e: Exception) {
                        seedDatabase()
                    }
                } else {
                    seedDatabase()
                }
            }
        }
    }

    fun updateUserLocation(lat: Double, lon: Double) {
        if (userLat == lat && userLon == lon) return // Evitar cálculos innecesarios
        userLat = lat
        userLon = lon
        _state.update { it.copy(userLatitude = lat, userLongitude = lon) }
        applyFilters()
    }

    fun getRandomTruck(): FoodTruck? {
        return allTrucks.filter { it.isOpen }.randomOrNull()
    }

    fun toggleFavorite(truck: FoodTruck) {
        viewModelScope.launch {
            val isFav = favoriteDao.isFavorite(truck.id).first()
            if (isFav) {
                favoriteDao.deleteById(truck.id)
            } else {
                favoriteDao.insertFavorite(
                    FavoriteTruckEntity(
                        id = truck.id,
                        name = truck.name,
                        category = truck.category,
                        rating = truck.rating,
                        distance = truck.distance,
                        isOpen = truck.isOpen
                    )
                )
            }
        }
    }

    fun filterByCategory(category: String?) {
        selectedCategory = if (category == "Todos") null else category
        applyFilters()
    }

    fun searchTrucks(query: String) {
        searchQuery = query
        applyFilters()
    }

    private fun applyFilters() {
        var filtered = allTrucks
        
        if (!selectedCategory.isNullOrBlank()) {
            filtered = filtered.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
        
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        val currentLat = userLat
        val currentLon = userLon

        // Optimizamos: Calculamos la distancia numérica una sola vez por item
        val trucksWithCalculatedDistance = filtered.map { truck ->
            val distanceInMeters = if (currentLat != null && currentLon != null) {
                calculateDistanceInMeters(currentLat, currentLon, truck.latitude, truck.longitude)
            } else null

            Pair(truck, distanceInMeters)
        }

        val sortedList = if (currentLat != null && currentLon != null) {
            trucksWithCalculatedDistance
                .sortedBy { it.second ?: Double.MAX_VALUE }
                .map { (truck, distMeters) ->
                    val distanceLabel = if (distMeters != null) {
                        if (distMeters >= 1000) {
                            "~${(distMeters / 1000.0).toOneDecimal()} km"
                        } else {
                            "~${distMeters.toInt()} m"
                        }
                    } else truck.distance

                    truck.copy(distance = distanceLabel)
                }
        } else {
            trucksWithCalculatedDistance.map { it.first }
        }

        _state.update { it.copy(
            foodTrucks = sortedList,
            suggestions = allTrucks.filter { it.isPromo },
            isLoading = false
        ) }
    }

    private fun calculateDistanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0
        val dLat = (lat2 - lat1) * PI / 180.0
        val dLon = (lon2 - lon1) * PI / 180.0
        val a = sin(dLat / 2).pow(2) +
                cos(lat1 * PI / 180.0) * cos(lat2 * PI / 180.0) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    private fun Double.toOneDecimal(): String {
        return (round(this * 10) / 10.0).toString()
    }

    private fun seedDatabase() {
        val mockTrucks = listOf(
            FoodTruck(
                id = "1",
                name = "El Chori Loco",
                category = "Hamburguesas",
                rating = "4.8",
                reviewsCount = "234",
                distance = "0.5 km",
                promoText = "2x1 en clásicas",
                isPromo = true,
                description = "Las mejores hamburguesas artesanales de Cochabamba",
                latitude = -17.366,
                longitude = -66.153,
                isOpen = true,
                menu = listOf(MenuDish("Hamburguesa Clásica", "25"), MenuDish("Hamburguesa Especial", "35")),
                userReviews = listOf(UserReview("Carlos M.", 5, "¡Excelente!"))
            ),
            FoodTruck(
                id = "2",
                name = "Pizza del Carrito",
                category = "Pizza",
                rating = "4.6",
                reviewsCount = "122",
                distance = "1.2 km",
                promoText = "Pizza + Soda",
                isPromo = false,
                description = "Pizza artesanal a la leña en movimiento.",
                latitude = -17.382,
                longitude = -66.145,
                isOpen = true,
                menu = listOf(MenuDish("Pepperoni", "45")),
                userReviews = listOf(UserReview("Pedro R.", 5, "Delicioso."))
            )
        )
        viewModelScope.launch {
            try {
                val data = json.encodeToString(mockTrucks)
                firebaseManager.saveData("food_trucks_v3", data)
            } catch (e: Exception) {
                println("Error seeding: ${e.message}")
            }
        }
    }
}
