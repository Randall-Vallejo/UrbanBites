package com.ucb.app.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.core.preferences.DistanceUnit
import com.ucb.app.core.preferences.UserPreferences
import com.ucb.app.core.session.UserSession
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.home.presentation.state.HomeUiState
import com.ucb.app.home.domain.model.FoodTruck
import com.ucb.app.home.domain.model.MenuDish
import com.ucb.app.home.domain.model.UserReview
import com.ucb.app.home.data.db.dao.FavoriteDao
import com.ucb.app.home.data.db.entity.FavoriteTruckEntity
import com.ucb.app.core.notification.NotificationProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.*

class HomeViewModel(
    private val firebaseManager: FirebaseManager,
    private val favoriteDao: FavoriteDao,
    private val notificationProvider: NotificationProvider
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private var allTrucks: List<FoodTruck> = emptyList()
    private var selectedCategory: String? = null
    private var searchQuery: String = ""
    private var userLat: Double? = null
    private var userLon: Double? = null

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    val favorites = favoriteDao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeFoodTrucks()
        observeUserSession()
        observePreferences()
    }

    private fun observePreferences() {
        viewModelScope.launch {
            UserPreferences.distanceUnit.collect { applyFilters() }
        }
    }

    private fun observeUserSession() {
        viewModelScope.launch {
            UserSession.userName.collect { name ->
                _state.update { it.copy(userName = name) }
            }
        }
    }

    private fun observeFoodTrucks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            firebaseManager.observeData("food_trucks_v3").collect { jsonData ->
                if (jsonData != null && jsonData != "null") {
                    try {
                        allTrucks = json.decodeFromString<List<FoodTruck>>(jsonData)
                        applyFilters()
                    } catch (e: Exception) { _state.update { it.copy(isLoading = false) } }
                } else { _state.update { it.copy(isLoading = false) } }
            }
        }
    }

    fun updateUserLocation(lat: Double, lon: Double) {
        userLat = lat; userLon = lon
        _state.update { it.copy(userLatitude = lat, userLongitude = lon) }
        applyFilters()
    }

    fun addReview(truckId: String, stars: Int, comment: String) {
        viewModelScope.launch {
            val truckIndex = allTrucks.indexOfFirst { it.id == truckId }
            if (truckIndex != -1) {
                val truck = allTrucks[truckIndex]
                val newReview = UserReview(
                    userName = UserSession.userName.value,
                    stars = stars,
                    comment = comment
                )
                val updatedReviews = truck.userReviews.toMutableList().apply { add(newReview) }
                
                val newRating = updatedReviews.map { it.stars }.average()
                val updatedTruck = truck.copy(
                    userReviews = updatedReviews,
                    rating = newRating.toOneDecimal(),
                    reviewsCount = updatedReviews.size.toString()
                )
                
                val updatedList = allTrucks.toMutableList().apply { set(truckIndex, updatedTruck) }
                firebaseManager.saveData("food_trucks_v3", json.encodeToString(updatedList))
                
                notificationProvider.showLocalNotification("¡Gracias!", "Tu reseña para ${truck.name} ha sido publicada.")
            }
        }
    }

    fun getRandomTruck(): FoodTruck? = allTrucks.filter { it.isOpen }.randomOrNull()

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
                        isOpen = truck.isOpen,
                        imageUrl = truck.imageUrl
                    )
                )
                if (UserPreferences.localFavoritesEnabled.value) {
                    notificationProvider.showLocalNotification("¡Favorito!", "Añadiste ${truck.name}")
                }
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
        if (!selectedCategory.isNullOrBlank()) filtered = filtered.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        if (searchQuery.isNotBlank()) filtered = filtered.filter { it.name.contains(searchQuery, ignoreCase = true) }

        val currentLat = userLat
        val currentLon = userLon
        val unit = UserPreferences.distanceUnit.value

        val sortedList = if (currentLat != null && currentLon != null) {
            filtered.map { truck ->
                val distMeters = calculateDistanceInMeters(currentLat, currentLon, truck.latitude, truck.longitude)
                val label = formatDistance(distMeters, unit)
                truck.copy(distance = label) to distMeters
            }.sortedBy { it.second }.map { it.first }
        } else filtered

        _state.update { it.copy(foodTrucks = sortedList, suggestions = allTrucks.filter { it.isPromo }, isLoading = false) }
    }

    private fun formatDistance(meters: Double, unit: DistanceUnit): String {
        return if (unit == DistanceUnit.KM) {
            if (meters >= 1000) "${(meters / 1000.0).toOneDecimal()} km" else "${meters.toInt()} m"
        } else {
            val miles = meters * 0.000621371
            "${miles.toOneDecimal()} mi"
        }
    }

    private fun calculateDistanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371000.0
        val dLat = (lat2 - lat1) * PI / 180.0
        val dLon = (lon2 - lon1) * PI / 180.0
        val a = sin(dLat / 2).pow(2) + cos(lat1 * PI / 180.0) * cos(lat2 * PI / 180.0) * sin(dLon / 2).pow(2)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }

    private fun Double.toOneDecimal(): String = (round(this * 10) / 10.0).toString()
}
