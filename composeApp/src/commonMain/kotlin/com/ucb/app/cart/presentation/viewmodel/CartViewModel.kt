package com.ucb.app.cart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.core.data.db.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            repository.addToCart(productId)
        }
    }
}