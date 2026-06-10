package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.core.preferences.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(onBack: () -> Unit) {
    val fcmEnabled by UserPreferences.fcmEnabled.collectAsState()
    val proximityEnabled by UserPreferences.proximityAlertsEnabled.collectAsState()
    val localEnabled by UserPreferences.localFavoritesEnabled.collectAsState()

    val orangeColor = Color(0xFFFF5722)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Notificaciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás", tint = orangeColor)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background) // Adaptable a Modo Oscuro
                .padding(24.dp)
        ) {
            Text(
                "Gestiona tus preferencias de alertas. Elige qué tipo de notificaciones deseas recibir en tu dispositivo.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            NotificationSwitchItem(
                title = "Promociones y Descuentos",
                subtitle = "Alertas externas desde Firebase (FCM)",
                checked = fcmEnabled,
                onCheckedChange = { UserPreferences.setFcmEnabled(it) }
            )

            NotificationSwitchItem(
                title = "Alertas de Cercanía",
                subtitle = "Revisión de carritos cercanos en segundo plano",
                checked = proximityEnabled,
                onCheckedChange = { UserPreferences.setProximityEnabled(it) }
            )

            NotificationSwitchItem(
                title = "Actividad de Favoritos",
                subtitle = "Avisos internos generados por el teléfono",
                checked = localEnabled,
                onCheckedChange = { UserPreferences.setLocalEnabled(it) }
            )
        }
    }
}

@Composable
fun NotificationSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFFF5722)
                )
            )
        }
    }
}
