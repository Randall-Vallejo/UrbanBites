package com.ucb.app.maps.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@Composable
actual fun MapScreen(
    modifier: Modifier,
    latitude: Double?,
    longitude: Double?,
    title: String?
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    
    // Si recibimos coordenadas, centramos ahí, si no, en Cbba por defecto
    val initialPos = if (latitude != null && longitude != null) {
        LatLng(latitude, longitude)
    } else {
        LatLng(-17.37, -66.15)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 15f)
    }

    // Efecto para mover la cámara si las coordenadas cambian
    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 16f)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLocation = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
        ) {
            // Marcador del Food Truck (si hay coordenadas)
            if (latitude != null && longitude != null) {
                Marker(
                    state = MarkerState(position = LatLng(latitude, longitude)),
                    title = title ?: "Food Truck",
                    snippet = "¡Estamos aquí!"
                )
            }

            // Marcador del usuario
            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Tú estás aquí",
                    icon = null // Se podría usar un icono azul
                )
            }
        }

        SmallFloatingActionButton(
            onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            val pos = LatLng(it.latitude, it.longitude)
                            userLocation = pos
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(pos, 15f)
                        }
                    }
                } else {
                    permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = Color.White,
            contentColor = Color(0xFFFF5722)
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "Mi Ubicación")
        }
    }
}
