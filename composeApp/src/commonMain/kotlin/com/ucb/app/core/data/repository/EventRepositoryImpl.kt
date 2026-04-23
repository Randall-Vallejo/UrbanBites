package com.ucb.app.core.data.repository

import com.ucb.app.core.data.db.dao.EventDao
import com.ucb.app.core.data.db.entity.EventEntity
import com.ucb.app.core.domain.repository.EventRepository
import com.ucb.app.firebase.data.datasource.FirebaseManager

class EventRepositoryImpl(
    private val eventDao: EventDao,
    private val firebaseManager: FirebaseManager
) : EventRepository {

    override suspend fun recordEvent(type: String) {
        val event = EventEntity(
            type = type,
            timestamp = 0L, // En un entorno real usaríamos una función de plataforma para el tiempo
            isSynced = false
        )
        eventDao.insertEvent(event)
    }

    override suspend fun syncEventsWithFirebase() {
        val unsyncedEvents = eventDao.getUnsyncedEvents()
        unsyncedEvents.forEach { event ->
            try {
                // Formato de ruta en Firebase: events/OPEN_ID
                val path = "events/${event.type}_${event.id}"
                val data = "Type: ${event.type}, ID: ${event.id}"
                
                firebaseManager.saveData(path, data)
                
                // Si se subió con éxito, marcamos como sincronizado
                eventDao.updateEvent(event.copy(isSynced = true))
            } catch (e: Exception) {
                // Si falla uno, seguimos con el siguiente
            }
        }
    }
}
