package com.ucb.app.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_configs")
data class ConfigEntity(
    @PrimaryKey val key: String,
    val value: String,
    val timestamp: Long = 0L
)
