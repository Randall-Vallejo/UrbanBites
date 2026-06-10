package com.ucb.app.core.data.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Tasks
import com.ucb.app.core.data.notification.LocalNotificationHelper
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.home.domain.model.FoodTruck
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import kotlin.math.*

class CheckNearbyTrucksWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val firebaseManager = FirebaseManager()
    private val notificationHelper = LocalNotificationHelper(context)
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun doWork(): Result {
        Log.d("CheckNearbyTrucksWorker", "Iniciando verificación de proximidad...")

        // 1. Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CheckNearbyTrucksWorker", "Permisos de ubicación denegados para el Worker")
            return Result.failure()
        }

        return try {
            // 2. Obtener ubicación actual de forma síncrona/await
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val location = Tasks.await(fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null))

            if (location != null) {
                val userLat = location.latitude
                val userLon = location.longitude
                Log.d("CheckNearbyTrucksWorker", "Ubicación obtenida: $userLat, $userLon")

                // 3. Obtener carritos de Firebase (tomamos el primer valor del Flow)
                val jsonData = firebaseManager.observeData("food_trucks_v3").firstOrNull()
                
                if (!jsonData.isNullOrEmpty() && jsonData != "null") {
                    val trucks = json.decodeFromString<List<FoodTruck>>(jsonData)
                    
                    // 4. Buscar carritos a menos de 500 metros
                    val nearbyTruck = trucks.find { truck ->
                        val distance = calculateDistanceInMeters(
                            userLat, userLon,
                            truck.latitude, truck.longitude
                        )
                        distance <= 500.0 // REQUERIMIENTO: 500 metros
                    }

                    // 5. Si hay uno cerca, disparar notificación
                    nearbyTruck?.let {
                        notificationHelper.showNotification(
                            "¡Hambre detectada! 🍕",
                            "${it.name} está a menos de 500 metros de ti. ¡Aprovecha!"
                        )
                        Log.d("CheckNearbyTrucksWorker", "Notificación enviada para: ${it.name}")
                    }
                }
            } else {
                Log.w("CheckNearbyTrucksWorker", "No se pudo obtener la ubicación (location null)")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("CheckNearbyTrucksWorker", "Error ejecutando verificación: ${e.message}")
            Result.retry()
        }
    }

    private fun calculateDistanceInMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0
        val dLat = (lat2 - lat1) * PI / 180.0
        val dLon = (lon2 - lon1) * PI / 180.0
        val a = sin(dLat / 2).pow(2) +
                cos(lat1 * PI / 180.0) * cos(lat2 * PI / 180.0) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}
