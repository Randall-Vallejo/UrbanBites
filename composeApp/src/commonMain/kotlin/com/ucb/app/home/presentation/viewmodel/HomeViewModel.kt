package com.ucb.app.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.home.presentation.state.HomeUiState
import com.ucb.app.home.presentation.screen.FoodTruck
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
            
            firebaseManager.observeData("food_trucks").collect { jsonData ->
                if (json != null && jsonData != null && jsonData != "null") {
                    try {
                        // En Firebase Realtime, si guardamos una lista, a veces viene como Map o List
                        // Para simplificar esta demo, asumimos que es una lista codificada como String
                        val trucks = json.decodeFromString<List<FoodTruck>>(jsonData)
                        _state.update { it.copy(
                            foodTrucks = trucks,
                            suggestions = trucks.filter { it.isPromo },
                            isLoading = false
                        ) }
                    } catch (e: Exception) {
                        // Si falla el parseo, subimos los datos por defecto (Seeding)
                        seedDatabase()
                    }
                } else {
                    // Si no hay datos, sembramos la base de datos
                    seedDatabase()
                }
            }
        }
    }

    private fun seedDatabase() {
        val mockTrucks = listOf(
            FoodTruck("El Chori Loco", "Hamburguesas", "4.8", "234", "0.5 km", "2x1 en clásicas", true),
            FoodTruck("Pizza del Carrito", "Pizza", "4.6", "122", "1.2 km", "Pizza + Soda", false),
            FoodTruck("Taco Truck", "Tacos", "4.7", "201", "2.0 km", "5 tacos x 30 Bs", true),
            FoodTruck("Pollos Copacabana", "Pollo", "4.9", "500", "3.5 km", "Combo Familiar", false)
        )
        
        viewModelScope.launch {
            try {
                val data = json.encodeToString(mockTrucks)
                firebaseManager.saveData("food_trucks", data)
            } catch (e: Exception) {
                println("Error seeding database: ${e.message}")
            }
        }
    }
}
