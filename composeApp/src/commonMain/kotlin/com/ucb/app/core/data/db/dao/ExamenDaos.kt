package com.ucb.app.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.app.core.data.db.ConfigEntity
import com.ucb.app.core.data.db.EventEntity
import com.ucb.app.core.data.db.RemoteConfigHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Query("SELECT * FROM cached_config WHERE `key` = :key LIMIT 1")
    suspend fun getConfig(key: String): ConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: ConfigEntity)
}

@Dao
interface RemoteConfigHistoryDao {
    @Query("SELECT * FROM remote_config_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<RemoteConfigHistoryEntity>>

    @Insert
    suspend fun insertHistory(item: RemoteConfigHistoryEntity)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM app_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Insert
    suspend fun insertEvent(event: EventEntity)
}
