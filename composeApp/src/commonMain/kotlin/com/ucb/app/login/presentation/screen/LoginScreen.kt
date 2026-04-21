package com.ucb.app.login.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ucb.app.login.presentation.state.LoginEvent
import com.ucb.app.login.presentation.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import urbanbites.composeapp.generated.resources.Res
import urbanbites.composeapp.generated.resources.login_btn
import urbanbites.composeapp.generated.resources.login_email
import urbanbites.composeapp.generated.resources.login_password
import urbanbites.composeapp.generated.resources.login_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import urbanbites.composeapp.generated.resources.urban_bites_logo


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    // 1. Cambiamos uiState por state (como está en tu ViewModel)
    // Agrega el import: import androidx.compose.runtime.collectAsState
    val uiState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo (Asegúrate de poner la imagen en drawables como te dije antes)
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

        // 2. Campo de Email usando Eventos
        OutlinedTextField(
            value = uiState.email, // Antes era state.email
            onValueChange = { viewModel.onEvent(LoginEvent.OnEmailChanged(it)) }, // Usamos el evento del ViewModel
            label = { Text("Correo electrónico / Usuario") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Campo de Contraseña
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.onEvent(LoginEvent.OnPasswordChanged(it)) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. Botón Iniciar Sesión
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