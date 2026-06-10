package com.ucb.app.home.presentation.state

import com.ucb.app.home.domain.model.MenuDish

data class AddFoodTruckUiState(
    val name: String = "",
    val openingTime: String = "",
    val closingTime: String = "",
    val latitude: Double = -17.37,
    val longitude: Double = -66.15,
    val imageUrl: String = "",
    val specialty: String = "",
    val menuItems: List<MenuDish> = listOf(MenuDish()),
    val promoTitle: String = "",
    val promoDescription: String = "",
    val comboTitle: String = "",
    val comboDescription: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
