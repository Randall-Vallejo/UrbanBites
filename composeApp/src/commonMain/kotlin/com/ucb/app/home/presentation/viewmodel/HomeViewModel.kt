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

class HomeViewModel(
    private val firebaseManager: FirebaseManager,
    private val favoriteDao: FavoriteDao
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private var allTrucks: List<FoodTruck> = emptyList()
    private var selectedCategory: String? = null
    private var searchQuery: String = ""

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
            firebaseManager.observeData("food_truck_v3").collect { jsonData ->
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
        _state.update { it.copy(
            foodTrucks = filtered,
            suggestions = allTrucks.filter { it.isPromo },
            isLoading = false
        ) }
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
                menu = listOf(
                    MenuDish("Hamburguesa Clásica", "25"),
                    MenuDish("Hamburguesa Especial", "35"),
                    MenuDish("Papas Fritas", "12")
                ),
                userReviews = listOf(
                    UserReview("Carlos M.", 5, "¡Excelente! La mejor comida callejera."),
                    UserReview("Ana L.", 4, "Muy bueno, recomendado.")
                )
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
                menu = listOf(
                    MenuDish("Pepperoni", "45"),
                    MenuDish("Margarita", "40")
                ),
                userReviews = listOf(
                    UserReview("Pedro R.", 5, "Delicioso y buen precio.")
                )
            )
        )
        viewModelScope.launch {
            try {
                val data = json.encodeToString(mockTrucks)
                firebaseManager.saveData("food_truck_v3", data)
            } catch (e: Exception) {
                println("Error seeding: ${e.message}")
            }
        }
    }
}
