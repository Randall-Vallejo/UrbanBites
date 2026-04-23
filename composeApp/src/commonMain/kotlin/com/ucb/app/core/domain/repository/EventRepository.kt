package com.ucb.app.core.domain.repository

import com.ucb.app.core.data.db.entity.EventEntity

interface EventRepository {
    suspend fun recordEvent(type: String)
    suspend fun syncEventsWithFirebase()
}
