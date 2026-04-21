package com.ucb.app.cart.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ucb.app.cart.presentation.viewmodel.CartViewModel

@Composable
fun CartScreen(viewModel: CartViewModel) {
    Column {
        Button(
            onClick = {
                viewModel.addToCart(1) // ejemplo producto
            }
        ) {
            Text("Agregar al carrito")
        }
    }
}