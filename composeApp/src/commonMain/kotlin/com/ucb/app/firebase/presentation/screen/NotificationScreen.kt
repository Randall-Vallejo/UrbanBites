package com.ucb.app.firebase.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ucb.app.firebase.presentation.viewmodel.NotificationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
expect fun RequestNotificationPermission()

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = koinViewModel()
) {
    val token by viewModel.token.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    RequestNotificationPermission()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Prueba de Notificaciones Push",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        if (token.isEmpty()) {
            CircularProgressIndicator()
            Text("Obteniendo Token...")
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        clipboardManager.setText(AnnotatedString(token))
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TU TOKEN (Cópialo exacto):",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = token,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { viewModel.fetchToken() }) {
            Text("Verificar Token")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "PASOS:\n1. Copia el token de arriba.\n2. En Firebase, dale a 'Enviar mensaje de prueba'.\n3. PEGA el token en el cuadro que dice 'Añadir un token de registro de FCM'.\n4. Dale al botón (+) y luego a 'Probar'.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
