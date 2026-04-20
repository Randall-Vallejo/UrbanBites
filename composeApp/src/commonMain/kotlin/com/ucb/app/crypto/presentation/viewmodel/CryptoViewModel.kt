package com.ucb.app.crypto.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.app.crypto.domain.model.CryptoModel
import com.ucb.app.crypto.presentation.state.CryptoEffect
import com.ucb.app.crypto.presentation.state.CryptoEvent
import com.ucb.app.crypto.presentation.state.CryptoState
import com.ucb.app.firebase.data.datasource.FirebaseManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val firebaseManager: FirebaseManager
): ViewModel() {
    private val _state = MutableStateFlow(CryptoState())
    val state = _state.asStateFlow()

    private val _effect = Channel<CryptoEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: CryptoEvent) {
        when (event) {
            CryptoEvent.OnLoad -> loadCrypto()
            CryptoEvent.OnRefresh -> loadCrypto()
        }
    }

    fun saveTestToFirebase() {
        viewModelScope.launch {
            firebaseManager.saveData("test/urban_bites", "Hola desde UrbanBites!")
        }
    }

    private fun loadCrypto() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            delay(1000)

            val dummyCryptoList = listOf(
                CryptoModel("bitcoin", "Bitcoin", "BTC",
                    "https://coin-images.coingecko.com/coins/images/1/large/bitcoin.png",
                    70611.0, 1, -0.11, 71300.0, 68970.0
                ),
                CryptoModel("ethereum", "Ethereum", "ETH",
                    "https://coin-images.coingecko.com/coins/images/279/large/ethereum.png",
                    2154.56, 2, 0.62, 2172.6, 2104.86
                ),
                CryptoModel("tether", "Tether", "USDT",
                    "https://coin-images.coingecko.com/coins/images/325/large/Tether.png",
                    0.999, 3, -0.01, 1.0, 0.99
                ),
                CryptoModel("binancecoin", "BNB", "BNB",
                    "https://coin-images.coingecko.com/coins/images/825/large/bnb-icon2_2x.png",
                    639.31, 4, 0.28, 639.68, 627.4
                ),
                CryptoModel("ripple", "XRP", "XRP",
                    "https://coin-images.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png",
                    1.42, 5, -0.50, 1.43, 1.39
                ),
                CryptoModel("usd-coin", "USDC", "USDC",
                    "https://coin-images.coingecko.com/coins/images/6319/large/USDC.png",
                    0.999, 6, -0.01, 1.0, 0.99
                )
            )

            _state.update {
                it.copy(
                    isLoading = false,
                    cryptos = dummyCryptoList
                )
            }
        }
    }
}
