package com.ucb.app.firebase.data.datasource

expect class FirebaseManager() {
    suspend fun saveData(path: String, value: String)
}
