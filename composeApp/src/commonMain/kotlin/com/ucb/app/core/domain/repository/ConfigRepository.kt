package com.ucb.app.core.domain.repository

interface ConfigRepository {
    suspend fun getAppConfig(key: String): String
}
