package com.ucb.app.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.app.core.data.db.entity.ConfigEntity

@Dao
interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: ConfigEntity)

    @Query("SELECT * FROM app_configs WHERE `key` = :key LIMIT 1")
    suspend fun getConfigByKey(key: String): ConfigEntity?
}
