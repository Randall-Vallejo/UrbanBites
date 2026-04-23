package com.ucb.app.demo.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.demo.presentation.viewmodel.DemoViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.ucb.app.Res
import com.ucb.app.login_btn
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoFuncionalidadesScreen(
    viewModel: DemoViewModel = koinViewModel(),
    onShowLocalNotification: () -> Unit,
    onRunWorker: () -> Unit,
    fcmToken: String
) {
    val uiState by viewModel.state.collectAsState()
    
    LaunchedEffect(fcmToken) {
        viewModel.setFcmToken(fcmToken)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Examen Final - UrbanBites") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // EJERCICIO 1: SINCRONIZACIÓN (Remote Config + Room)
            item {
                DemoSection(title = "EJERCICIO 1: Sincronización (Remote Config + Room)") {
                    Text("Demostración de descarga inicial y caché local persistente.", fontSize = 12.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Valor en la Nube (Remote Config):", fontWeight = FontWeight.Bold)
                    Text(uiState.remoteConfigWelcome, fontSize = 18.sp, color = Color(0xFF00796B))
                    
                    Spacer(Modifier.height(8.dp))
                    Text("Valor en Caché Local (Room):", fontWeight = FontWeight.Bold)
                    Text(uiState.localConfigValue, fontSize = 14.sp, color = Color.Gray)
                    
                    Button(
                        onClick = { viewModel.syncWelcomeMessage() }, 
                        modifier = Modifier.padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Sincronizar Ahora")
                    }
                }
            }

            // EJERCICIO 2: WORKER + NOTIFICACIÓN
            item {
                DemoSection(title = "EJERCICIO 2: Background Services (Worker + Notificación)") {
                    Text("Vigilancia en segundo plano: detecta cambios y notifica localmente.", fontSize = 12.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Estado del Servicio:", fontWeight = FontWeight.Bold)
                    val statusColor = when {
                        uiState.workerResult.contains("Completado") -> Color(0xFF2E7D32)
                        uiState.workerResult.contains("Error") -> Color.Red
                        uiState.workerResult.contains("Ejecutando") -> Color(0xFFF57C00)
                        else -> Color.Gray
                    }
                    Text(uiState.workerResult, color = statusColor, fontWeight = FontWeight.Medium)
                    
                    Button(
                        onClick = { viewModel.runWorkerDemo(onRunWorker) }, 
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Ejecutar Worker Ahora")
                    }
                }
            }

            // SECCIÓN ADICIONAL: ROOM MANUAL
            item {
                DemoSection(title = "Sección: Room (Persistencia Local Manual)") {
                    OutlinedTextField(
                        value = uiState.roomInput,
                        onValueChange = { viewModel.onRoomInputChange(it) },
                        label = { Text("Dato para Room") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(Modifier.padding(top = 8.dp)) {
                        Button(onClick = { viewModel.saveToRoom() }) { Text("Guardar") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { viewModel.clearRoom() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Limpiar") }
                    }
                    Text("Lista de Items guardados:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    uiState.roomItems.forEach { item ->
                        Text("- ${item.content}", fontSize = 14.sp)
                    }
                }
            }

            // SECCIÓN ADICIONAL: REALTIME DB
            item {
                DemoSection(title = "Sección: Firebase Realtime Database") {
                    OutlinedTextField(
                        value = uiState.firebaseInput,
                        onValueChange = { viewModel.onFirebaseInputChange(it) },
                        label = { Text("Mensaje a Firebase") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = { viewModel.saveToFirebase() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Enviar a Realtime")
                    }
                    Text("Último valor escuchado en vivo:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Text(uiState.firebaseLastValue, color = Color.Blue)
                }
            }

            // SECCIÓN ADICIONAL: NOTIFICACIONES
            item {
                DemoSection(title = "Sección: Notificaciones") {
                    Text("Notificación Interna (Local):", fontWeight = FontWeight.Bold)
                    Button(
                        onClick = onShowLocalNotification, 
                        modifier = Modifier.padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Lanzar Notificación Local")
                    }
                    
                    Text("Notificación Externa (Token FCM):", fontWeight = FontWeight.Bold)
                    SelectionContainer {
                        Text(uiState.fcmToken, fontSize = 10.sp, color = Color.Gray, lineHeight = 12.sp)
                    }
                }
            }

            // SECCIÓN ADICIONAL: TRADUCCIONES
            item {
                DemoSection(title = "Sección: Localize (Traducciones)") {
                    Text("Texto en el idioma del sistema:", fontWeight = FontWeight.Bold)
                    Text(stringResource(Res.string.login_btn), fontSize = 20.sp, color = Color(0xFFFF6D00))
                }
            }
        }
    }
}

@Composable
fun DemoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            content()
        }
    }
}

@Composable
fun SelectionContainer(content: @Composable () -> Unit) {
    androidx.compose.foundation.text.selection.SelectionContainer {
        content()
    }
}
