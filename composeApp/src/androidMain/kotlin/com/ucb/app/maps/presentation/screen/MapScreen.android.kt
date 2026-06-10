package com.ucb.app.maps.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ucb.app.home.domain.model.FoodTruck
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
actual fun MapScreen(
    modifier: Modifier,
    trucks: List<FoodTruck>,
    onTruckClick: (String) -> Unit,
    centerLatitude: Double?,
    centerLongitude: Double?,
    onLocationResult: (Double, Double) -> Unit,
    isPickerMode: Boolean,
    onMapClick: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current 
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    
    var hasLocationPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(centerLatitude ?: -17.38, centerLongitude ?: -66.15), 14f)
    }

    Box(
        modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    // Si el usuario toca o desliza sobre el mapa, bloqueamos el scroll del formulario principal
                    if (event.changes.any { it.pressed }) {
                        view.parent?.requestDisallowInterceptTouchEvent(true)
                    }
                }
            }
        }
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false, 
                myLocationButtonEnabled = false,
                scrollGesturesEnabled = true, // Activamos gestos internos
                scrollGesturesEnabledDuringRotateOrZoom = true
            ),
            onMapClick = { latLng ->
                if (isPickerMode) onMapClick(latLng.latitude, latLng.longitude)
            }
        ) {
            if (!isPickerMode) {
                trucks.forEach { truck ->
                    Marker(
                        state = MarkerState(position = LatLng(truck.latitude, truck.longitude)),
                        title = truck.name,
                        onClick = { onTruckClick(truck.name); true }
                    )
                }
            } else if (centerLatitude != null && centerLongitude != null) {
                Marker(state = MarkerState(position = LatLng(centerLatitude, centerLongitude)))
            }
        }

        SmallFloatingActionButton(
            onClick = {
                if (hasLocationPermission) {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { location ->
                        location?.let {
                            val pos = LatLng(it.latitude, it.longitude)
                            if (isPickerMode) onMapClick(it.latitude, it.longitude)
                            scope.launch { cameraPositionState.animate(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(pos, 16f)) }
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = Color.White,
            contentColor = Color(0xFFFF5722)
        ) { Icon(Icons.Default.LocationSearching, null) }
    }
}
