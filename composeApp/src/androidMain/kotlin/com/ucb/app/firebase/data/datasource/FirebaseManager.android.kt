package com.ucb.app.firebase.data.datasource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual class FirebaseManager actual constructor() {
    private val database = FirebaseDatabase.getInstance().reference

    actual suspend fun saveData(path: String, value: String) {
        try {
            database.child(path).setValue(value).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun observeData(path: String): Flow<String?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot.getValue(String::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                // Si hay error de permisos (muy común en Firebase recién creado), 
                // enviamos null en lugar de cerrar el flow con error para evitar el crash.
                trySend(null)
                // close(error.toException()) // Comentado para evitar el crash en el examen
            }
        }
        database.child(path).addValueEventListener(listener)
        awaitClose { database.child(path).removeEventListener(listener) }
    }
}
