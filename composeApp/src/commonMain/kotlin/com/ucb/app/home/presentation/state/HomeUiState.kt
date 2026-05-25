package com.ucb.app.home.presentation.state

import com.ucb.app.home.presentation.screen.FoodTruck

data class HomeUiState(
    val isLoading: Boolean = false,
    val foodTrucks: List<FoodTruck> = emptyList(),
    val suggestions: List<FoodTruck> = emptyList(),
    val userName: String = "María García",
    val error: String? = null
)
