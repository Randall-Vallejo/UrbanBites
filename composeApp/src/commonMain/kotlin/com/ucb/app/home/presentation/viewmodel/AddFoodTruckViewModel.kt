package com.ucb.app.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.home.domain.model.FoodTruck
import com.ucb.app.home.domain.model.MenuDish
import com.ucb.app.home.domain.repository.FoodTruckRepository
import com.ucb.app.home.presentation.state.AddFoodTruckUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddFoodTruckViewModel(
    private val repository: FoodTruckRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddFoodTruckUiState())
    val state = _state.asStateFlow()

    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onOpeningTimeChange(value: String) = _state.update { it.copy(openingTime = value) }
    fun onClosingTimeChange(value: String) = _state.update { it.copy(closingTime = value) }
    fun onLocationChange(lat: Double, lon: Double) = _state.update { it.copy(latitude = lat, longitude = lon) }
    fun onImageUrlChange(value: String) = _state.update { it.copy(imageUrl = value) }
    fun onSpecialtyChange(value: String) = _state.update { it.copy(specialty = value) }
    
    fun onPromoTitleChange(value: String) = _state.update { it.copy(promoTitle = value) }
    fun onPromoDescriptionChange(value: String) = _state.update { it.copy(promoDescription = value) }
    fun onComboTitleChange(value: String) = _state.update { it.copy(comboTitle = value) }
    fun onComboDescriptionChange(value: String) = _state.update { it.copy(comboDescription = value) }

    // --- Menu Dinámico ---
    fun addMenuItem() {
        val newList = _state.value.menuItems.toMutableList()
        newList.add(MenuDish())
        _state.update { it.copy(menuItems = newList) }
    }

    fun updateMenuItem(index: Int, name: String, price: String) {
        val newList = _state.value.menuItems.toMutableList()
        if (index in newList.indices) {
            newList[index] = MenuDish(name, price)
            _state.update { it.copy(menuItems = newList) }
        }
    }

    fun removeMenuItem(index: Int) {
        val newList = _state.value.menuItems.toMutableList()
        if (newList.size > 1 && index in newList.indices) {
            newList.removeAt(index)
            _state.update { it.copy(menuItems = newList) }
        }
    }

    fun saveFoodTruck() {
        val currentState = _state.value
        
        if (currentState.name.isBlank() || currentState.openingTime.isBlank() ||
            currentState.closingTime.isBlank() || currentState.imageUrl.isBlank() || 
            currentState.specialty.isBlank() || currentState.menuItems.any { it.name.isBlank() || it.price.isBlank() }) {
            _state.update { it.copy(errorMessage = "Por favor completa todos los campos obligatorios (*) incluyendo la URL de la imagen") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val truck = FoodTruck(
                    name = currentState.name,
                    openingTime = currentState.openingTime,
                    closingTime = currentState.closingTime,
                    latitude = currentState.latitude,
                    longitude = currentState.longitude,
                    imageUrl = currentState.imageUrl,
                    specialty = currentState.specialty,
                    menu = currentState.menuItems,
                    promoTitle = currentState.promoTitle,
                    promoDescription = currentState.promoDescription,
                    comboTitle = currentState.comboTitle,
                    comboDescription = currentState.comboDescription,
                    isPromo = currentState.promoTitle.isNotBlank()
                )
                repository.insertFoodTruck(truck)
                _state.update { AddFoodTruckUiState(isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = "Error al guardar: ${e.message}") }
            }
        }
    }
    
    fun resetSuccess() {
        _state.update { it.copy(isSuccess = false) }
    }
}
