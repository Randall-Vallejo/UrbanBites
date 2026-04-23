package com.ucb.app.core.domain.repository

import com.ucb.app.core.data.db.AppDatabase
import com.ucb.app.core.data.db.entity.ConfigEntity
import com.ucb.app.core.data.db.entity.EventEntity
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.firebase.data.datasource.RemoteConfigManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SyncRepository(
    private val database: AppDatabase,
    private val firebaseManager: FirebaseManager,
    private val remoteConfigManager: RemoteConfigManager,
) {

    
    fun getWelcomeMessage(): Flow<String> {
        return database.configDao().getConfigByKey("welcome_message")
            .map { it?.value ?: "Cargando..." }
    }

    suspend fun syncRemoteConfig(): Boolean {
        return try {
            val success = remoteConfigManager.fetchAndActivate()
            if (success) {
                val message = remoteConfigManager.getString("welcome_message")
                database.configDao().insertConfig(ConfigEntity("welcome_message",
                    message))
            }
            success
        } catch (ignore: Exception) {
            false
        }
    }

    suspend fun logEvent(type: String, timestamp: Long) {
        val event = EventEntity(
            timestamp = timestamp,
            type = type
        )
        database.eventDao().insertEvent(event)
    }

    suspend fun syncEventsToFirebase() {
        val unsynced = database.eventDao().getUnsyncedEvents()
        println("SyncRepository: Sincronizando ${unsynced.size} eventos")
        unsynced.forEach { event ->
            try {
                val path = "eventsHuayna/${event.timestamp}"
                val message = if (event.type == "OPEN") "App Abierta" else "App Cerrada"
                
                println("SyncRepository: Subiendo evento ${event.type} a $path")
                firebaseManager.saveData(path, message)
                database.eventDao().updateEvents(listOf(event.copy(synced = true)))
                println("SyncRepository: Evento ${event.id} sincronizado correctamente")
            } catch (e: Exception) {
                println("SyncRepository: Error al sincronizar evento ${event.id}: ${e.message}")
            }
        }
    }
}
