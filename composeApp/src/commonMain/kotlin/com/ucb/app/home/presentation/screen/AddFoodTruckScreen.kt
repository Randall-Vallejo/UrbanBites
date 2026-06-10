package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.home.presentation.viewmodel.AddFoodTruckViewModel
import com.ucb.app.maps.presentation.screen.MapScreen
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodTruckScreen(
    onBack: () -> Unit,
    viewModel: AddFoodTruckViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val orangeColor = Color(0xFFFF5722)
    
    var showTimePicker by remember { mutableStateOf<String?>(null) }
    var isMapTouched by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onBack()
            viewModel.resetSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Negocio", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            userScrollEnabled = !isMapTouched 
        ) {
            item {
                SectionHeader("Información General", orangeColor)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Nombre del Food Truck *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = state.specialty,
                    onValueChange = viewModel::onSpecialtyChange,
                    label = { Text("Especialidad Principal *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                SectionHeader("Imagen del Carrito *", orangeColor)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.imageUrl,
                    onValueChange = viewModel::onImageUrlChange,
                    label = { Text("URL de la Foto del Food Truck *") },
                    supportingText = { Text("(ej: https://enlace.com/imagen.jpg)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                SectionHeader("Horarios de Atención *", orangeColor)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TimePickerButton(label = "Apertura", value = state.openingTime, onClick = { showTimePicker = "opening" }, modifier = Modifier.weight(1f))
                    TimePickerButton(label = "Cierre", value = state.closingTime, onClick = { showTimePicker = "closing" }, modifier = Modifier.weight(1f))
                }
            }

            item {
                SectionHeader("Ubicación exacta *", orangeColor)
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    isMapTouched = event.changes.any { it.pressed }
                                }
                            }
                        }
                ) {
                    MapScreen(
                        modifier = Modifier.fillMaxSize(),
                        isPickerMode = true,
                        centerLatitude = state.latitude,
                        centerLongitude = state.longitude,
                        onMapClick = { lat, lon -> viewModel.onLocationChange(lat, lon) }
                    )
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SectionHeader("Menú Destacado *", orangeColor, Modifier.weight(1f))
                    IconButton(onClick = { viewModel.addMenuItem() }) { Icon(Icons.Default.AddCircle, null, tint = orangeColor) }
                }
            }

            items(state.menuItems.size) { index ->
                val dish = state.menuItems[index]
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = dish.name, onValueChange = { viewModel.updateMenuItem(index, it, dish.price) }, label = { Text("Plato") }, modifier = Modifier.weight(2f), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = dish.price, onValueChange = { viewModel.updateMenuItem(index, dish.name, it) }, label = { Text("Bs.") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                    if (state.menuItems.size > 1) {
                        IconButton(onClick = { viewModel.removeMenuItem(index) }) { Icon(Icons.Default.RemoveCircleOutline, null, tint = Color.Gray) }
                    }
                }
            }

            item {
                SectionHeader("Promociones y Combos (Opcional)", orangeColor)
                Spacer(Modifier.height(8.dp))
                PromotionInput(label = "Promoción", bgColor = Color(0xFFFFF3E0), title = state.promoTitle, description = state.promoDescription, onTitleChange = viewModel::onPromoTitleChange, onDescChange = viewModel::onPromoDescriptionChange)
            }

            item {
                PromotionInput(label = "Combo del Día", bgColor = Color(0xFFFFFDE7), title = state.comboTitle, description = state.comboDescription, onTitleChange = viewModel::onComboTitleChange, onDescChange = viewModel::onComboDescriptionChange)
            }

            item {
                if (state.errorMessage != null) {
                    Text(state.errorMessage!!, color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = viewModel::saveFoodTruck,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !state.isLoading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = orangeColor)
                ) {
                    if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Publicar Food Truck", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showTimePicker != null) {
        val timeState = rememberTimePickerState(is24Hour = true)
        AlertDialog(
            onDismissRequest = { showTimePicker = null },
            confirmButton = {
                TextButton(onClick = {
                    val formatted = "${timeState.hour.toString().padStart(2, '0')}:${timeState.minute.toString().padStart(2, '0')}"
                    if (showTimePicker == "opening") viewModel.onOpeningTimeChange(formatted)
                    else viewModel.onClosingTimeChange(formatted)
                    showTimePicker = null
                }) { Text("Confirmar") }
            },
            text = { TimePicker(state = timeState) }
        )
    }
}

@Composable
fun SectionHeader(text: String, color: Color, modifier: Modifier = Modifier) {
    Text(text, color = color, fontWeight = FontWeight.Black, fontSize = 15.sp, modifier = modifier)
}

@Composable
fun TimePickerButton(label: String, value: String, onClick: () -> Unit, modifier: Modifier) {
    OutlinedCard(onClick = onClick, modifier = modifier.height(64.dp), shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(label, fontSize = 10.sp, color = Color.Gray)
            Text(if (value.isEmpty()) "--:--" else value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PromotionInput(label: String, bgColor: Color, title: String, description: String, onTitleChange: (String) -> Unit, onDescChange: (String) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = bgColor), shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(bottom = 12.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            OutlinedTextField(value = title, onValueChange = onTitleChange, label = { Text("Título") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
            OutlinedTextField(value = description, onValueChange = onDescChange, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White, focusedContainerColor = Color.White))
        }
    }
}
