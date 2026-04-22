package com.ucb.app.firebase.data.datasource

import kotlinx.coroutines.flow.Flow

expect class FirebaseManager() {
    suspend fun saveData(path: String, value: String)
    fun observeData(path: String): Flow<String?>
}
