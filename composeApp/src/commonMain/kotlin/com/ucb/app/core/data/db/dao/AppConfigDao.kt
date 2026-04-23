package com.ucb.app.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.app.core.data.db.entity.AppConfigEntity

@Dao
interface AppConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: AppConfigEntity)

    @Query("SELECT * FROM app_config WHERE `key` = :configKey LIMIT 1")
    suspend fun getConfig(configKey: String): AppConfigEntity?
}
