package com.ucb.app.firebase.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.firebase.presentation.viewmodel.NotificationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
expect fun RequestNotificationPermission()

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = koinViewModel()
) {
    val token by viewModel.token.collectAsState()
    val cloudMessage by viewModel.cloudMessage.collectAsState()
    val messageToSend by viewModel.messageToSend.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    RequestNotificationPermission()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // MENSAJE DESDE FIREBASE REMOTE CONFIG
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Mensaje de la Nube:", color = Color.White, fontSize = 12.sp)
                Text(
                    text = cloudMessage,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = "Realtime Database Chat",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // CAMPO DE TEXTO PARA ESCRIBIR EL MENSAJE
        OutlinedTextField(
            value = messageToSend,
            onValueChange = { viewModel.onMessageChange(it) },
            label = { Text("Escribe un mensaje para Firebase") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.sendCustomMessage() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            enabled = messageToSend.isNotEmpty()
        ) {
            Text("Enviar Mensaje Escrito")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (token.isEmpty()) {
            CircularProgressIndicator()
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        clipboardManager.setText(AnnotatedString(token))
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TU TOKEN FCM (Click para copiar):",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = token,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { 
                viewModel.fetchToken()
                viewModel.fetchRemoteConfig()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar Todo")
        }
    }
}
