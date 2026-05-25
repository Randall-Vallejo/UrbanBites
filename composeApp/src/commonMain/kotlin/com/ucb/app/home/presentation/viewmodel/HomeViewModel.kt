package com.ucb.app.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.home.presentation.state.HomeUiState
import com.ucb.app.home.domain.model.FoodTruck
import com.ucb.app.home.domain.model.MenuDish
import com.ucb.app.home.domain.model.UserReview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HomeViewModel(
    private val firebaseManager: FirebaseManager
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

    init {
        observeFoodTrucks()
    }

    private fun observeFoodTrucks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            // Escuchamos la versión 3 de la BD que tiene todos los campos
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
        
        // 1. Filtrar por Categoría
        if (!selectedCategory.isNullOrBlank()) {
            filtered = filtered.filter { 
                it.category.equals(selectedCategory, ignoreCase = true) 
            }
        }
        
        // 2. Filtrar por Nombre (Buscador)
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { 
                it.name.contains(searchQuery, ignoreCase = true) 
            }
        }

        // Actualizamos el estado para que la UI (Mapa y Lista) se refresquen
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
                firebaseManager.saveData("food_trucks_v3", data)
            } catch (e: Exception) {
                println("Error seeding: ${e.message}")
            }
        }
    }
}
