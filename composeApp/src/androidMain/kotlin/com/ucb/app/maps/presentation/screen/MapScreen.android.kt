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
import androidx.compose.material.icons.filled.LocationSearching
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
import com.ucb.app.home.domain.model.FoodTruck

@SuppressLint("MissingPermission")
@Composable
actual fun MapScreen(
    modifier: Modifier,
    trucks: List<FoodTruck>,
    onTruckClick: (String) -> Unit,
    centerLatitude: Double?,
    centerLongitude: Double?
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    
    val initialPos = if (centerLatitude != null && centerLongitude != null) {
        LatLng(centerLatitude, centerLongitude)
    } else {
        LatLng(-17.3833, -66.15) 
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 14f)
    }

    LaunchedEffect(centerLatitude, centerLongitude) {
        if (centerLatitude != null && centerLongitude != null) {
            cameraPositionState.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                    LatLng(centerLatitude, centerLongitude), 16f
                )
            )
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let { userLocation = LatLng(it.latitude, it.longitude) }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
        ) {
            trucks.forEach { truck ->
                Marker(
                    state = MarkerState(position = LatLng(truck.latitude, truck.longitude)),
                    title = truck.name,
                    snippet = "${truck.category} • ${truck.rating} ⭐",
                    onClick = {
                        onTruckClick(truck.name)
                        true
                    }
                )
            }

            userLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Tú estás aquí",
                    alpha = 0.7f
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
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 100.dp, end = 16.dp),
            containerColor = Color.White,
            contentColor = Color(0xFFFF5722)
        ) {
            Icon(Icons.Default.LocationSearching, contentDescription = "Mi Ubicación")
        }
    }
}
