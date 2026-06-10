package com.ucb.app.home.data.repository

import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.home.domain.model.FoodTruck
import com.ucb.app.home.domain.repository.FoodTruckRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FoodTruckRepositoryImpl(
    private val firebaseManager: FirebaseManager
) : FoodTruckRepository {
    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
    private val path = "food_trucks_v3"

    override suspend fun insertFoodTruck(truck: FoodTruck) {
        val currentData = firebaseManager.observeData(path).first()
        val currentList = if (currentData != null && currentData != "null") {
            try {
                json.decodeFromString<List<FoodTruck>>(currentData).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }
        } else {
            mutableListOf()
        }

        val newTruck = truck.copy(id = (currentList.size + 1).toString())
        currentList.add(newTruck)
        
        val updatedJson = json.encodeToString(currentList)
        firebaseManager.saveData(path, updatedJson)
    }
}
