package com.ucb.app.firebase.data.datasource

expect class RemoteConfigManager() {
    suspend fun fetchAndActivate(): Boolean
    fun getString(key: String): String
}
