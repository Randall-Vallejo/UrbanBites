package com.ucb.app.login.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ucb.app.login.presentation.state.LoginEvent
import com.ucb.app.login.presentation.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.painterResource
import urbanbites.composeapp.generated.resources.Res
import urbanbites.composeapp.generated.resources.urban_bites_logo
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit // Añadido para la navegación
) {
    val uiState by viewModel.state.collectAsState()

    // Este bloque detecta cuando el ViewModel marca que el login fue exitoso
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo UrbanBites
        Image(
            painter = painterResource(Res.drawable.urban_bites_logo),
            contentDescription = null,
            modifier = Modifier.size(60.dp).align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Icono de Usuario
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Email
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEvent(LoginEvent.OnEmailChanged(it)) },
            label = { Text("Correo electrónico / Usuario") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onEvent(LoginEvent.OnPasswordChanged(it)) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar error si existe (Ej. "Usuario no encontrado")
        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Botón Iniciar Sesión
        Button(
            onClick = { viewModel.onEvent(LoginEvent.OnClick) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6D00)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("INICIAR SESIÓN", color = Color.White)
        }
    }
}