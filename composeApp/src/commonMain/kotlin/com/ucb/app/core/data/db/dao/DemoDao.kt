package com.ucb.app.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.app.core.data.db.entity.DemoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDemoItem(item: DemoEntity)

    @Query("SELECT * FROM demo_items ORDER BY timestamp DESC")
    fun getAllDemoItems(): Flow<List<DemoEntity>>

    @Query("DELETE FROM demo_items")
    suspend fun deleteAll()
}
