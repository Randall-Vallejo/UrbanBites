package com.ucb.app.demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.core.data.db.AppDatabase
import com.ucb.app.core.data.db.RemoteConfigHistoryEntity
import com.ucb.app.core.data.db.entity.DemoEntity
import com.ucb.app.demo.presentation.state.DemoUiState
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.firebase.data.datasource.RemoteConfigManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DemoViewModel(
    private val database: AppDatabase,
    private val firebaseManager: FirebaseManager,
    private val remoteConfigManager: RemoteConfigManager
) : ViewModel() {

    private val _state = MutableStateFlow(DemoUiState())
    val state = _state.asStateFlow()

    init {
        observeRoom()
        observeEvents()
        observeConfigHistory()
        observeFirebase()
        fetchRemoteConfig()
        loadCachedConfig()
    }

    // --- ROOM (Demo Items) ---
    private fun observeRoom() {
        viewModelScope.launch {
            database.demoDao().getAllDemoItems().collect { items ->
                _state.update { it.copy(roomItems = items) }
            }
        }
    }

    // --- ROOM (Events from Background - PREGUNTA 4) ---
    private fun observeEvents() {
        viewModelScope.launch {
            database.eventDao().getAllEvents().collect { events ->
                _state.update { it.copy(eventItems = events) }
            }
        }
    }

    // --- ROOM (Remote Config History - PREGUNTA 1) ---
    private fun observeConfigHistory() {
        viewModelScope.launch {
            database.remoteConfigHistoryDao().getAllHistory().collect { history ->
                _state.update { it.copy(configHistory = history) }
            }
        }
    }

    // --- ROOM (Cached Remote Config) ---
    private fun loadCachedConfig() {
        viewModelScope.launch {
            val cachedValue = remoteConfigManager.getCachedValue("welcome_message")
            _state.update { it.copy(cachedWelcomeMessage = cachedValue) }
        }
    }

    fun onRoomInputChange(value: String) {
        _state.update { it.copy(roomInput = value) }
    }

    fun saveToRoom() {
        val content = _state.value.roomInput
        if (content.isNotBlank()) {
            viewModelScope.launch {
                database.demoDao().insertDemoItem(
                    DemoEntity(
                        content = content, 
                        timestamp = 0L 
                    )
                )
                _state.update { it.copy(roomInput = "") }
            }
        }
    }

    fun clearRoom() {
        viewModelScope.launch {
            database.demoDao().deleteAll()
        }
    }

    // --- FIREBASE REALTIME ---
    private fun observeFirebase() {
        viewModelScope.launch {
            firebaseManager.observeData("demo_path").collect { value ->
                _state.update { it.copy(firebaseLastValue = value ?: "Sin datos") }
            }
        }
    }

    fun onFirebaseInputChange(value: String) {
        _state.update { it.copy(firebaseInput = value) }
    }

    fun saveToFirebase() {
        val content = _state.value.firebaseInput
        if (content.isNotBlank()) {
            viewModelScope.launch {
                firebaseManager.saveData("demo_path", content)
                _state.update { it.copy(firebaseInput = "") }
            }
        }
    }

    // --- REMOTE CONFIG ---
    fun fetchRemoteConfig() {
        viewModelScope.launch {
            _state.update { it.copy(remoteConfigWelcome = "Descargando...") }
            if (remoteConfigManager.fetchAndActivate()) {
                val msg = remoteConfigManager.getString("welcome_message")
                _state.update { it.copy(remoteConfigWelcome = msg) }
                
                // GUARDAMOS EN EL HISTORIAL DE ROOM (PUNTO 1)
                database.remoteConfigHistoryDao().insertHistory(
                    RemoteConfigHistoryEntity(value = msg, timestamp = System.currentTimeMillis())
                )

                loadCachedConfig() // Actualizamos la vista del caché
            }
        }
    }

    fun setFcmToken(token: String) {
        _state.update { it.copy(fcmToken = token) }
    }
    
    fun runWorkerDemo(action: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(workerResult = "Ejecutando...") }
            try {
                action()
                delay(1000)
                _state.update { it.copy(workerResult = "Completado correctamente") }
            } catch (e: Exception) {
                _state.update { it.copy(workerResult = "Error: ${e.message}") }
            }
        }
    }
}
