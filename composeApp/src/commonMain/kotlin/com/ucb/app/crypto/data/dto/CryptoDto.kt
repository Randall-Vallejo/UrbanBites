package com.ucb.app.crypto.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CryptoDto(
    @SerialName("id") val id: String,
    @SerialName("symbol") val symbol: String,
    @SerialName("name") val name: String,
    @SerialName("image") val image: String,
    @SerialName("current_price") val currentPrice: Double,
    @SerialName("market_cap") val marketCap: Double,
    @SerialName("market_cap_rank") val marketCapRank: Int,
    @SerialName("total_volume") val totalVolume: Double,
    @SerialName("high_24h") val high24h: Double,
    @SerialName("low_24h") val low24h: Double,
    @SerialName("price_change_percentage_24h") val priceChangePercentage24h: Double,
    @SerialName("circulating_supply") val circulatingSupply: Double,
    @SerialName("max_supply") val maxSupply: Double? = null,
    @SerialName("ath") val ath: Double,
    @SerialName("atl") val atl: Double
)
