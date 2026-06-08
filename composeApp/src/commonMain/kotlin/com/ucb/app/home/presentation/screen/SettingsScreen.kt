package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.core.preferences.AppTheme
import com.ucb.app.core.preferences.DistanceUnit
import com.ucb.app.core.preferences.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenSystemSettings: () -> Unit
) {
    val theme by UserPreferences.appTheme.collectAsState()
    val distanceUnit by UserPreferences.distanceUnit.collectAsState()
    val orangeColor = Color(0xFFFF5722)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // FUERZA EL FONDO OSCURO
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Configuración", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, null, tint = orangeColor)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            SectionHeader("Apariencia")
            ThemeSelector(theme) { UserPreferences.setTheme(it) }

            Spacer(Modifier.height(32.dp))

            SectionHeader("Unidades de Medida")
            DistanceUnitSelector(distanceUnit) { UserPreferences.setDistanceUnit(it) }

            Spacer(Modifier.height(32.dp))

            SectionHeader("Sistema")
            SettingsActionItem(
                title = "Permiso de Ubicación",
                subtitle = "Ir a ajustes para activar GPS",
                icon = Icons.Default.GpsFixed,
                onClick = onOpenSystemSettings
            )

            Spacer(Modifier.height(32.dp))

            SectionHeader("Información")
            SettingsActionItem(
                title = "Términos de Servicio",
                subtitle = "Políticas de privacidad y uso",
                icon = Icons.Default.Description
            )

            Spacer(Modifier.weight(1f))
            
            Text(
                "UrbanBites Build v1.0.42 (Stable) \n © 2026",
                modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color(0xFFFF5722),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 13.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun ThemeSelector(currentTheme: AppTheme, onThemeSelected: (AppTheme) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ThemeChip("Claro", Icons.Default.LightMode, currentTheme == AppTheme.LIGHT) { onThemeSelected(AppTheme.LIGHT) }
        ThemeChip("Oscuro", Icons.Default.DarkMode, currentTheme == AppTheme.DARK) { onThemeSelected(AppTheme.DARK) }
        ThemeChip("Sistema", Icons.Default.SettingsSuggest, currentTheme == AppTheme.SYSTEM) { onThemeSelected(AppTheme.SYSTEM) }
    }
}

@Composable
fun RowScope.ThemeChip(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        leadingIcon = { Icon(icon, null, Modifier.size(16.dp)) },
        modifier = Modifier.weight(1f),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFFFF5722).copy(alpha = 0.2f),
            selectedLabelColor = Color(0xFFFF5722),
            selectedLeadingIconColor = Color(0xFFFF5722),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun DistanceUnitSelector(currentUnit: DistanceUnit, onUnitSelected: (DistanceUnit) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Kilómetros / Metros", Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                RadioButton(
                    selected = currentUnit == DistanceUnit.KM, 
                    onClick = { onUnitSelected(DistanceUnit.KM) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF5722))
                )
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Millas / Pies", Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                RadioButton(
                    selected = currentUnit == DistanceUnit.MILES, 
                    onClick = { onUnitSelected(DistanceUnit.MILES) },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF5722))
                )
            }
        }
    }
}

@Composable
fun SettingsActionItem(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), fontSize = 12.sp)
            }
            Icon(Icons.Default.OpenInNew, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f), modifier = Modifier.size(16.dp))
        }
    }
}
