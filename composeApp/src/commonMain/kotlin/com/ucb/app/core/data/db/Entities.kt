package com.ucb.app.core.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_config")
data class ConfigEntity(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "remote_config_history")
data class RemoteConfigHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: String,
    val timestamp: Long
)

@Entity(tableName = "app_events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val type: String
)
