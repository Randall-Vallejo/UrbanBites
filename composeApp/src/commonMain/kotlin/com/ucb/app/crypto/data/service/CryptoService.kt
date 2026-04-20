package com.ucb.app.crypto.data.service

import com.ucb.app.crypto.data.datasource.CryptoRemoteDatasource
import com.ucb.app.crypto.data.dto.CryptoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CryptoService : CryptoRemoteDatasource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun getList(): List<CryptoDto> {
        return try {
            client.get("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd").body()
        } catch (e: Exception) {
            throw e
        }
    }
}
