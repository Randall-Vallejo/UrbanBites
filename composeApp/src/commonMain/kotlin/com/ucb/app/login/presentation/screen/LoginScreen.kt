package com.ucb.app.login.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.login.presentation.state.LoginEffect
import com.ucb.app.login.presentation.state.LoginEvent
import com.ucb.app.login.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Colores UrbanBites extraídos del diseño Figma
    val orangeColor = Color(0xFFFF5722)
    val redColor = Color(0xFFE64A19)
    val lightOrange = Color(0xFFFF8A65)
    val surfaceGray = Color(0xFFF5F5F5)

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            if (effect is LoginEffect.NavigateToHome) onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(orangeColor, redColor)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo Card con Icono
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = orangeColor,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "UrbanBites",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Descubre la mejor comida callejera",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Form Card
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Bienvenido de vuelta!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "Inicia sesión para explorar food trucks",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Correo
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Correo electrónico", 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color.DarkGray, 
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { viewModel.onEvent(LoginEvent.OnEmailChanged(it)) },
                            placeholder = { Text("tu@email.com") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = orangeColor) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = surfaceGray,
                                unfocusedContainerColor = surfaceGray,
                                focusedIndicatorColor = orangeColor,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contraseña
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Contraseña", 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color.DarkGray, 
                            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.onEvent(LoginEvent.OnPasswordChanged(it)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = orangeColor) },
                            trailingIcon = {
                                IconButton(onClick = { viewModel.onEvent(LoginEvent.OnTogglePasswordVisibilityClicked) }) {
                                    Icon(
                                        imageVector = if (uiState.isPasswordVisible) Icons.Default.Close else Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = surfaceGray,
                                unfocusedContainerColor = surfaceGray,
                                focusedIndicatorColor = orangeColor,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                    }

                    if (uiState.error != null) {
                        val errorText = when (uiState.error) {
                            "EMPTY_EMAIL" -> "Correo vacío"
                            "EMPTY_PASSWORD" -> "Contraseña vacía"
                            "INVALID_CREDENTIALS" -> "Credenciales inválidas"
                            else -> uiState.error!!
                        }
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp).align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón Login
                    Button(
                        onClick = { viewModel.onEvent(LoginEvent.OnLoginClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        enabled = !uiState.isLoading
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(listOf(lightOrange, orangeColor)),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text(
                                    text = "Iniciar sesión",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón Registro - CORREGIDO: Ahora llama a onNavigateToRegister
                    Button(
                        onClick = { onNavigateToRegister() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEEEEEE),
                            contentColor = orangeColor
                        )
                    ) {
                        Text(
                            text = "Registrarse",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Opción de Invitado
            TextButton(onClick = { viewModel.onEvent(LoginEvent.OnContinueAsGuestClicked) }) {
                Text(
                    text = "Continuar como invitado",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "© 2024 UrbanBites · Cochabamba, Bolivia",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
