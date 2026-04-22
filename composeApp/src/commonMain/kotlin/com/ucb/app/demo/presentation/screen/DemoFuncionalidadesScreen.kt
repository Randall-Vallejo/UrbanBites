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
            TopAppBar(title = { Text("Demo Examen - UrbanBites") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. ROOM
            item {
                DemoSection(title = "1. Room (Persistencia Local)") {
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
                    Text("Items en Room:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    uiState.roomItems.forEach { item ->
                        Text("- ${item.content}", fontSize = 14.sp)
                    }
                }
            }

            // 2. FIREBASE REALTIME
            item {
                DemoSection(title = "2. Firebase Realtime Database") {
                    OutlinedTextField(
                        value = uiState.firebaseInput,
                        onValueChange = { viewModel.onFirebaseInputChange(it) },
                        label = { Text("Mensaje a Firebase") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = { viewModel.saveToFirebase() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Enviar a Realtime")
                    }
                    Text("Último valor (Realtime):", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Text(uiState.firebaseLastValue, color = Color.Blue)
                }
            }

            // 3. REMOTE CONFIG
            item {
                DemoSection(title = "3. Firebase Remote Config") {
                    Text("Valor desde la nube:", fontWeight = FontWeight.Bold)
                    Text(uiState.remoteConfigWelcome, fontSize = 18.sp, color = Color(0xFF00796B))
                    Button(onClick = { viewModel.fetchRemoteConfig() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Actualizar Config")
                    }
                }
            }

            // 4. NOTIFICACIÓN INTERNA
            item {
                DemoSection(title = "4. Notificación Interna (Local)") {
                    Text("Prueba de notificación generada por la app sin servidor externo.")
                    Button(
                        onClick = onShowLocalNotification, 
                        modifier = Modifier.padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Lanzar Notificación Local")
                    }
                }
            }

            // 7. NOTIFICACIÓN EXTERNA (FCM)
            item {
                DemoSection(title = "7. Notificación Externa (FCM Push)") {
                    Text("Token FCM del dispositivo (Cópialo para enviar push):", fontWeight = FontWeight.Bold)
                    SelectionContainer {
                        Text(uiState.fcmToken, fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp)
                    }
                    Text(
                        "Usa este token en Firebase Console para probar envíos externos.",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // 5. SERVICIOS SEGUNDO PLANO
            item {
                DemoSection(title = "5. Background Services (Worker)") {
                    Text("Estado actual:", fontWeight = FontWeight.Bold)
                    val statusColor = when {
                        uiState.workerResult.contains("Completado") -> Color(0xFF2E7D32)
                        uiState.workerResult.contains("Error") -> Color.Red
                        uiState.workerResult.contains("Ejecutando") -> Color(0xFFF57C00)
                        else -> Color.Gray
                    }
                    Text(uiState.workerResult, color = statusColor, fontWeight = FontWeight.Medium)
                    Button(onClick = { viewModel.runWorkerDemo(onRunWorker) }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Ejecutar Worker Ahora")
                    }
                }
            }

            // 6. TRADUCCIONES
            item {
                DemoSection(title = "6. Localize (Traducciones)") {
                    Text("Texto obtenido de recursos (Res.string.login_btn):", fontWeight = FontWeight.Bold)
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
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
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
