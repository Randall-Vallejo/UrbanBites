package com.ucb.app.home.domain.repository

import com.ucb.app.home.domain.model.FoodTruck

interface FoodTruckRepository {
    suspend fun insertFoodTruck(truck: FoodTruck)
}
