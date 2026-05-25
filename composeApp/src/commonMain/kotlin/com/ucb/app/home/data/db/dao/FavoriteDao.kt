package com.ucb.app.home.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.app.home.data.db.entity.FavoriteTruckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_trucks")
    fun getAllFavorites(): Flow<List<FavoriteTruckEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_trucks WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(truck: FavoriteTruckEntity)

    @Delete
    suspend fun deleteFavorite(truck: FavoriteTruckEntity)

    @Query("DELETE FROM favorite_trucks WHERE id = :id")
    suspend fun deleteById(id: String)
}
