package com.ucb.app.home.presentation.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
actual fun DetailBottomActions(orange: Color, lat: Double, lng: Double, name: String) {
    val context = LocalContext.current

    Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // BOTÓN CÓMO LLEGAR (Requerimiento 3 - Intent Google Maps)
        Button(
            onClick = {
                val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            },
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D5A47)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.NearMe, null)
            Spacer(Modifier.width(8.dp))
            Text("Cómo llegar")
        }

        // BOTÓN COMPARTIR (Requerimiento 3 - Intent ACTION_SEND)
        OutlinedButton(
            onClick = {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "¡Mira este Food Truck en UrbanBites! Se llama '$name'. Ubicación: https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            },
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
