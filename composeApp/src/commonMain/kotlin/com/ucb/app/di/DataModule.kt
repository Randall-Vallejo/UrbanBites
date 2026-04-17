package com.ucb.app.di

import com.ucb.app.github.data.datasource.GitHubRemoteDataSource
import com.ucb.app.github.data.repository.GitHubRepositoryImpl
import com.ucb.app.github.data.service.GitHubService
import com.ucb.app.github.domain.repository.GitHubRepository
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

    single { GitHubService(get()) }
    single { GitHubRemoteDataSource(get()) }
    single<GitHubRepository> { GitHubRepositoryImpl(get()) }
}
