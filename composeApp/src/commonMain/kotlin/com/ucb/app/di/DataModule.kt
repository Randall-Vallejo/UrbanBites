package com.ucb.app.di

import com.ucb.app.core.data.db.AppDatabase
import com.ucb.app.core.data.db.getDatabaseBuilder
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.firebase.data.datasource.RemoteConfigManager
import com.ucb.app.login.data.repository.AuthenticationRepositoryImpl
import com.ucb.app.login.domain.repository.AuthenticationRepository
import io.ktor.client.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single {
        HttpClient {
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000
            }
        }
    }

    // Firebase Core & Remote Config
    single { FirebaseManager() }
    single { RemoteConfigManager() }

    // --- Room Database ---
    single<AppDatabase> {
        getDatabaseBuilder().build()
    }

    // --- Auth ---
    single<AuthenticationRepository> { AuthenticationRepositoryImpl() }
}
