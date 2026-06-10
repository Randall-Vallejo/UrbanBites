package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos de Servicio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Acuerdo de Uso de UrbanBites",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFF5722)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                "Bienvenido a UrbanBites. Al usar nuestra aplicación, aceptas de manera natural y humana los siguientes puntos para que todos podamos disfrutar de la mejor comida callejera en Cochabamba:\n\n" +
                "1. Respeto a la Comunidad: Las reseñas y comentarios deben ser honestos y constructivos. No toleramos el lenguaje ofensivo.\n\n" +
                "2. Información Real: Nos esforzamos por mantener los horarios y ubicaciones actualizados, pero recuerda que los Food Trucks son dinámicos por naturaleza.\n\n" +
                "3. Privacidad: Tus datos de localización se usan exclusivamente para mostrarte los carritos más cercanos y mejorar tu experiencia.\n\n" +
                "4. Responsabilidad: UrbanBites es una plataforma de conexión; la calidad y servicio final dependen de cada emprendedor gastronómico.\n\n" +
                "Estamos aquí para ayudarte a descubrir nuevos sabores y apoyar al comercio local.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            Divider(color = Color.LightGray, thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Desarrollado por el equipo de ingeniería integrado por:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val authors = listOf(
                "Hassan Lopez Olivares",
                "Randall Vallejo",
                "Rodrigo Villarroel",
                "Eduardo Huayna"
            )
            
            authors.forEach { author ->
                Text(
                    "• $author",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
