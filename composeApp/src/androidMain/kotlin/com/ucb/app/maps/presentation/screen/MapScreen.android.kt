package com.ucb.app.maps.presentation.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
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
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    
    // Estado para el icono, empieza nulo para evitar el crash inicial
    var truckIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }

    // Inicialización robusta con Callback oficial de Google Maps
    LaunchedEffect(context) {
        MapsInitializer.initialize(context, MapsInitializer.Renderer.LATEST, object : OnMapsSdkInitializedCallback {
            override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
                // Una vez confirmado que el SDK está listo, generamos el icono de forma segura
                truckIcon = try {
                    getEmojiBitmapDescriptor("🛻")
                } catch (e: Exception) {
                    null // Si falla, usará el marcador rojo estándar de Google
                }
            }
        })
    }

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
                scrollGesturesEnabled = true
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
                        icon = truckIcon, // Aplica el emoji solo cuando esté cargado
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
                            onLocationResult(it.latitude, it.longitude)
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

private fun getEmojiBitmapDescriptor(emoji: String): BitmapDescriptor {
    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
    paint.textSize = 90f
    val width = paint.measureText(emoji).toInt()
    val height = (-paint.ascent() + paint.descent()).toInt()
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawText(emoji, 0f, -paint.ascent(), paint)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
