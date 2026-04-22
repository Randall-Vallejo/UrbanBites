package com.ucb.app.cart.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ucb.app.cart.presentation.viewmodel.CartViewModel
import org.jetbrains.compose.resources.stringResource
import urbanbites.composeapp.generated.resources.Res
import urbanbites.composeapp.generated.resources.add_to_cart

@Composable
fun CartScreen(viewModel: CartViewModel) {
    Column {
        Button(
            onClick = {
                viewModel.addToCart(1) // ejemplo producto
            }
        ) {
            Text(stringResource(Res.string.add_to_cart))
        }
    }
}