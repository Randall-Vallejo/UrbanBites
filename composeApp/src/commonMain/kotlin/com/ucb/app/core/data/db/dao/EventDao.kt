package com.ucb.app.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ucb.app.core.data.db.entity.EventEntity

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM events WHERE isSynced = 0")
    suspend fun getUnsyncedEvents(): List<EventEntity>

    @Update
    suspend fun updateEvent(event: EventEntity)
}
