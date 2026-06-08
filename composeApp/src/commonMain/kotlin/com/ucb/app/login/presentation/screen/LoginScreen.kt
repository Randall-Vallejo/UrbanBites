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

    val orangeColor = Color(0xFFFF5722)
    val redColor = Color(0xFFE64A19)
    val lightOrange = Color(0xFFFF8A65)
    val surfaceGray = Color(0xFFF5F5F5)
    val fixedDarkColor = Color(0xFF333333) // Color negro/gris permanente

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            if (effect is LoginEffect.NavigateToHome) onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(orangeColor, redColor)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(Icons.Default.Home, null, tint = orangeColor, modifier = Modifier.size(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("UrbanBites", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
            Text("Descubre la mejor comida callejera", color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(32.dp))

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
                    Text("¡Bienvenido!", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = fixedDarkColor)
                    Text("Inicia sesión para continuar", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(Modifier.fillMaxWidth()) {
                        Text("Correo electrónico", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = fixedDarkColor)
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { viewModel.onEvent(LoginEvent.OnEmailChanged(it)) },
                            placeholder = { Text("tu@email.com") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = fixedDarkColor,
                                unfocusedTextColor = fixedDarkColor,
                                focusedBorderColor = orangeColor,
                                unfocusedContainerColor = surfaceGray,
                                focusedContainerColor = surfaceGray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(Modifier.fillMaxWidth()) {
                        Text("Contraseña", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = fixedDarkColor)
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.onEvent(LoginEvent.OnPasswordChanged(it)) },
                            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = fixedDarkColor,
                                unfocusedTextColor = fixedDarkColor,
                                focusedBorderColor = orangeColor,
                                unfocusedContainerColor = surfaceGray,
                                focusedContainerColor = surfaceGray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.onEvent(LoginEvent.OnLoginClicked) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = orangeColor),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        else Text("Iniciar sesión", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onNavigateToRegister) {
                        Text("¿No tienes cuenta? Regístrate aquí", color = orangeColor)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(onClick = { viewModel.onEvent(LoginEvent.OnContinueAsGuestClicked) }) {
                Text("Continuar como invitado", color = Color.White, textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Text("© 2026 UrbanBites · Cochabamba, Bolivia", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}
