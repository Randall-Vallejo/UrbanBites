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
import com.ucb.app.core.presentation.viewmodel.ConfigViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.ucb.app.Res
import com.ucb.app.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoFuncionalidadesScreen(
    viewModel: DemoViewModel = koinViewModel(),
    onShowLocalNotification: () -> Unit = {},
    onRunWorker: () -> Unit = {},
    fcmToken: String = ""
) {
    val uiState by viewModel.state.collectAsState()
    
    LaunchedEffect(fcmToken) {
        viewModel.setFcmToken(fcmToken)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(Res.string.demo_title)) })
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
                DemoSection(title = stringResource(Res.string.room_title)) {
                    OutlinedTextField(
                        value = uiState.roomInput,
                        onValueChange = { viewModel.onRoomInputChange(it) },
                        label = { Text(stringResource(Res.string.room_input_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(Modifier.padding(top = 8.dp)) {
                        Button(onClick = { viewModel.saveToRoom() }) { Text(stringResource(Res.string.save_btn)) }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { viewModel.clearRoom() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text(stringResource(Res.string.clear_btn)) }
                    }
                    Text(stringResource(Res.string.room_items_label), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    uiState.roomItems.forEach { item ->
                        Text("- ${item.content}", fontSize = 14.sp)
                    }
                }
            }

            // 2. FIREBASE REALTIME
            item {
                DemoSection(title = stringResource(Res.string.firebase_realtime_title)) {
                    OutlinedTextField(
                        value = uiState.firebaseInput,
                        onValueChange = { viewModel.onFirebaseInputChange(it) },
                        label = { Text(stringResource(Res.string.firebase_input_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = { viewModel.saveToFirebase() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text(stringResource(Res.string.send_realtime_btn))
                    }
                    Text(stringResource(Res.string.firebase_last_value_label), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                    Text(uiState.firebaseLastValue, color = Color.Blue)
                }
            }

            // 3. REMOTE CONFIG & CACHE (Ejercicio 1)
            item {
                val configViewModel: ConfigViewModel = koinViewModel()
                val configState by configViewModel.state.collectAsState()

                DemoSection(title = "3. Remote Config & Room Cache") {
                    Text("Valor desde la nube (o caché):", fontWeight = FontWeight.Bold)
                    if (configState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text(configState.configMessage, fontSize = 18.sp, color = Color(0xFF00796B))
                    }
                    Button(onClick = { configViewModel.loadConfig() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Sincronizar ahora")
                    }
                    Text(
                        "Nota: Si apagas el internet, mostrará el último valor guardado en Room.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // 4. NOTIFICACIÓN INTERNA
            item {
                DemoSection(title = stringResource(Res.string.local_notif_title)) {
                    Text(stringResource(Res.string.local_notif_desc))
                    Button(
                        onClick = onShowLocalNotification, 
                        modifier = Modifier.padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(stringResource(Res.string.launch_local_notif_btn))
                    }
                }
            }

            // 7. NOTIFICACIÓN EXTERNA (FCM)
            item {
                DemoSection(title = stringResource(Res.string.external_notif_title)) {
                    Text(stringResource(Res.string.fcm_token_device_label), fontWeight = FontWeight.Bold)
                    androidx.compose.foundation.text.selection.SelectionContainer {
                        Text(uiState.fcmToken, fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp)
                    }
                    Text(
                        stringResource(Res.string.fcm_instructions),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // 5. SERVICIOS SEGUNDO PLANO
            item {
                DemoSection(title = stringResource(Res.string.background_services_title)) {
                    Text(stringResource(Res.string.current_status_label), fontWeight = FontWeight.Bold)
                    val statusColor = when {
                        uiState.workerResult.contains("Completado") -> Color(0xFF2E7D32)
                        uiState.workerResult.contains("Error") -> Color.Red
                        uiState.workerResult.contains("Ejecutando") -> Color(0xFFF57C00)
                        else -> Color.Gray
                    }
                    Text(uiState.workerResult, color = statusColor, fontWeight = FontWeight.Medium)
                    Button(onClick = { viewModel.runWorkerDemo(onRunWorker) }, modifier = Modifier.padding(top = 8.dp)) {
                        Text(stringResource(Res.string.run_worker_btn))
                    }
                }
            }

            // 6. TRADUCCIONES
            item {
                DemoSection(title = stringResource(Res.string.localize_title)) {
                    Text(stringResource(Res.string.text_from_resources), fontWeight = FontWeight.Bold)
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
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}
