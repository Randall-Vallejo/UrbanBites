package com.ucb.app.demo.presentation.state

import com.ucb.app.core.data.db.entity.DemoEntity

data class DemoUiState(
    val roomInput: String = "",
    val roomItems: List<DemoEntity> = emptyList(),
    val firebaseInput: String = "",
    val firebaseLastValue: String = "Esperando...",
    val remoteConfigWelcome: String = "Cargando...",
    val fcmToken: String = "Obteniendo token...",
    val workerResult: String = "No ejecutado"
)
