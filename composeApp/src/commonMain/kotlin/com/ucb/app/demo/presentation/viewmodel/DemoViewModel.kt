package com.ucb.app.demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.core.data.db.AppDatabase
import com.ucb.app.core.data.db.entity.DemoEntity
import com.ucb.app.core.domain.repository.SyncRepository
import com.ucb.app.demo.presentation.state.DemoUiState
import com.ucb.app.firebase.data.datasource.FirebaseManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DemoViewModel(
    private val database: AppDatabase,
    private val firebaseManager: FirebaseManager,
    private val syncRepository: SyncRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DemoUiState())
    val state = _state.asStateFlow()

    init {
        observeRoom()
        observeFirebase()
        observeConfigCache()
    }

    // --- ROOM ---
    private fun observeRoom() {
        viewModelScope.launch {
            database.demoDao().getAllDemoItems().collect { items ->
                _state.update { it.copy(roomItems = items) }
            }
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

    // --- REMOTE CONFIG (Ejercicio 1) ---
    private fun observeConfigCache() {
        viewModelScope.launch {
            syncRepository.getWelcomeMessage().collect { msg ->
                _state.update { it.copy(remoteConfigWelcome = msg) }
            }
        }
    }

    fun fetchRemoteConfig() {
        viewModelScope.launch {
            syncRepository.syncRemoteConfig()
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
                delay(1000) // Simulación visual
                _state.update { it.copy(workerResult = "Completado correctamente") }
            } catch (e: Exception) {
                _state.update { it.copy(workerResult = "Error: ${e.message}") }
            }
        }
    }
}
