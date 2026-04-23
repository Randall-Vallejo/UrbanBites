package com.ucb.app.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_config")
data class AppConfigEntity(
    @PrimaryKey val key: String,
    val value: String,
    val lastUpdated: Long
)
