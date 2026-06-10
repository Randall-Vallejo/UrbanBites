package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
actual fun DetailBottomActions(orange: Color, lat: Double, lng: Double, name: String) {
    Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = { /* Implementar Deep Link para Mapas en iOS si es necesario */ },
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A47)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.NearMe, null)
            Spacer(Modifier.width(8.dp))
            Text("Cómo llegar")
        }

        OutlinedButton(
            onClick = { /* Implementar ShareSheet en iOS */ },
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF8A65))
        ) {
            Icon(Icons.Default.Share, null, tint = Color(0xFFFF8A65))
            Spacer(Modifier.width(8.dp))
            Text("Compartir", color = Color(0xFFFF8A65))
        }
    }
}
