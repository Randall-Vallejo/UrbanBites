package com.ucb.app.home.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_trucks")
data class FavoriteTruckEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val rating: String,
    val distance: String,
    val isOpen: Boolean
)
