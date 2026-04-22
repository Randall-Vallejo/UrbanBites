package com.ucb.app.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val totalPrice: Double,
    val timestamp: Long,
    val status: String // "PENDING", "COMPLETED"
)
