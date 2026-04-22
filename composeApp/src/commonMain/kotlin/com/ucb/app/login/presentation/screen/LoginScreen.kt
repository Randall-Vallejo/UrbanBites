package com.ucb.app.login.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import com.ucb.app.login.presentation.state.LoginEvent
import com.ucb.app.login.presentation.viewmodel.LoginViewModel
import com.ucb.app.Res
import com.ucb.app.login_btn
import com.ucb.app.login_email
import com.ucb.app.login_password
import com.ucb.app.login_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Column {
        Text(stringResource(Res.string.login_title))
        if (uiState.error != null) {
            Text(uiState.error!!, color = androidx.compose.ui.graphics.Color.Red)
        }
        TextField(
            onValueChange = {
                email = it
                viewModel.onEvent(LoginEvent.OnEmailChanged(it))
            },
            value = email,
            label = {
                Text(stringResource(Res.string.login_email))
            }
        )
        TextField(
            onValueChange = {
                password = it
                viewModel.onEvent(LoginEvent.OnPasswordChanged(it))
            },
            value = password,
            label = {
                Text(stringResource(Res.string.login_password))
            }
        )
        Button(onClick = {
            viewModel.onEvent(LoginEvent.OnClick)
        }) {
            Text(stringResource(Res.string.login_btn))
        }
    }
}
