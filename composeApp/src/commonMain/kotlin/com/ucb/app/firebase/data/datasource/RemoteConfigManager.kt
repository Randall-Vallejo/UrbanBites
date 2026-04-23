package com.ucb.app.firebase.data.datasource

import com.ucb.app.core.data.db.dao.ConfigDao
import com.ucb.app.core.data.db.ConfigEntity

expect class RemoteConfigManager(configDao: ConfigDao) {
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
    suspend fun getCachedValue(key: String): String
}
