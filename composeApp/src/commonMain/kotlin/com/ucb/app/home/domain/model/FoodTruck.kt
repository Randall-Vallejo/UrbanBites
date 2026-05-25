package com.ucb.app.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuDish(
    val name: String = "",
    val price: String = ""
)

@Serializable
data class UserReview(
    val userName: String = "",
    val stars: Int = 0,
    val comment: String = ""
)

@Serializable
data class FoodTruck(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val rating: String = "0.0",
    val reviewsCount: String = "0",
    val distance: String = "0.0 km",
    val promoText: String = "",
    val isPromo: Boolean = false,
    val isOpen: Boolean = true,
    val description: String = "",
    val latitude: Double = -17.37,
    val longitude: Double = -66.15,
    val menu: List<MenuDish> = emptyList(),
    val userReviews: List<UserReview> = emptyList()
)
