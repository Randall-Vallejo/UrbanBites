package com.ucb.app.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.core.data.db.AppDatabase
import com.ucb.app.firebase.data.datasource.RemoteConfigManager
import com.ucb.app.onboarding.data.db.OnboardingEntity
import com.ucb.app.onboarding.data.model.OnboardingConfig
import com.ucb.app.onboarding.data.model.OnboardingItem
import com.ucb.app.onboarding.presentation.state.OnboardingUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

class OnboardingViewModel(
    private val remoteConfigManager: RemoteConfigManager,
    private val database: AppDatabase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingUiState())
    val state = _state.asStateFlow()

    private val json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
        isLenient = true
    }

    init {
        fetchOnboardingConfig()
    }

    private fun fetchOnboardingConfig() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                remoteConfigManager.fetchAndActivate()
                val configJson = remoteConfigManager.getString("onboarding_config").trim()
                
                if (configJson.isNotEmpty() && configJson != "Cargando...") {
                    val items = try {
                        val jsonElement = json.parseToJsonElement(configJson)
                        if (jsonElement is JsonArray) {
                            json.decodeFromJsonElement<List<OnboardingItem>>(jsonElement)
                        } else if (jsonElement is JsonObject) {
                            // Si es un objeto, intentamos obtener la lista de la llave "onboarding_config"
                            // o decodificar el objeto completo OnboardingConfig
                            if (jsonElement.containsKey("onboarding_config")) {
                                json.decodeFromJsonElement<OnboardingConfig>(jsonElement).onboarding_config
                            } else {
                                // Caso borde: un solo item como objeto? No debería, pero por si acaso
                                listOf(json.decodeFromJsonElement<OnboardingItem>(jsonElement))
                            }
                        } else {
                            throw Exception("Formato JSON no reconocido")
                        }
                    } catch (e: Exception) {
                        throw Exception("Error al procesar JSON: ${e.message}\nInput: ${configJson.take(50)}...")
                    }
                    
                    _state.update { it.copy(items = items, isLoading = false, error = null) }
                } else {
                    _state.update { it.copy(error = "No hay datos en onboarding_config", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun onPageChanged(page: Int) {
        _state.update { it.copy(currentPage = page) }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            database.onboardingDao().setOnboardingCompleted(OnboardingEntity(isCompleted = true))
        }
    }
}
