package com.ucb.app.increment.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ucb.app.counter.presentation.viewmodel.CounterViewModel
import com.ucb.app.increment.presentation.viewmodel.IncrementViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IncrementScreen(viewModel: IncrementViewModel = koinViewModel()) {

    val state = viewModel.counterState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(state.value.toString())
        Button(
            onClick = {
                viewModel.incrementData()
            }
        ) {
            Text("Increment")
        }
    }
}