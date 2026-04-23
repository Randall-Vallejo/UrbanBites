package com.ucb.app.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.app.core.data.db.entity.ConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ConfigEntity)

    @Query("SELECT * FROM remote_configs WHERE `key` = :key LIMIT 1")
    fun getConfigByKey(key: String): Flow<ConfigEntity?>

    @Query("SELECT value FROM remote_configs WHERE `key` = :key LIMIT 1")
    suspend fun getValueByKey(key: String): String?
}
