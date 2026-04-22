package com.ucb.app.login.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}

    Column {
        Text(stringResource(Res.string.login_title))
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
        TextField( onValueChange = {
            viewModel.onEvent(LoginEvent.OnPasswordChanged(it))
        },
            value = password,
            label = {
                Text(stringResource(Res.string.login_password))
            })
        Button( onClick = {
            viewModel.onEvent(LoginEvent.OnClick)
        }) {
            Text(stringResource(Res.string.login_btn))
        }
    }
}
