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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.app.login.presentation.state.LoginEffect
import com.ucb.app.login.presentation.state.RegisterEvent
import com.ucb.app.login.presentation.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    val orangeColor = Color(0xFFFF5722)
    val redColor = Color(0xFFE64A19)
    val lightOrange = Color(0xFFFF8A65)
    val surfaceGray = Color(0xFFF5F5F5)

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            if (effect is LoginEffect.NavigateToHome) onRegisterSuccess()
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text("Crea tu cuenta", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

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
                    Text("¡Únete a UrbanBites!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                    Text("Registra tus datos para empezar", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                    Spacer(modifier = Modifier.height(24.dp))

                    RegisterField("Nombre completo", uiState.name, Icons.Default.Person, "Tu nombre") {
                        viewModel.onEvent(RegisterEvent.OnNameChanged(it))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField("Correo electrónico", uiState.email, Icons.Default.Email, "tu@email.com", KeyboardType.Email) {
                        viewModel.onEvent(RegisterEvent.OnEmailChanged(it))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField(
                        label = "Contraseña",
                        value = uiState.password,
                        icon = Icons.Default.Lock,
                        placeholder = "********",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        isPasswordVisible = uiState.isPasswordVisible,
                        onToggleVisibility = { viewModel.onEvent(RegisterEvent.OnTogglePasswordVisibilityClicked) }
                    ) {
                        viewModel.onEvent(RegisterEvent.OnPasswordChanged(it))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    RegisterField(
                        label = "Confirmar contraseña",
                        value = uiState.confirmPassword,
                        icon = Icons.Default.LockReset,
                        placeholder = "********",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        isPasswordVisible = uiState.isPasswordVisible
                    ) {
                        viewModel.onEvent(RegisterEvent.OnConfirmPasswordChanged(it))
                    }

                    if (uiState.error != null) {
                        Text(uiState.error!!, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.onEvent(RegisterEvent.OnRegisterClicked) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        enabled = !uiState.isLoading
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(lightOrange, orangeColor)), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            else Text("Registrarse", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterField(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onToggleVisibility: () -> Unit = {},
    onValueChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(icon, null, tint = Color(0xFFFF5722)) },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onToggleVisibility) {
                        Icon(if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray)
                    }
                }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFFF5722),
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
