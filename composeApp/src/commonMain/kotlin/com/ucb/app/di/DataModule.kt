package com.ucb.app.di

import com.ucb.app.core.data.db.AppDatabase
import com.ucb.app.core.data.db.getDatabaseBuilder
import com.ucb.app.core.data.db.repository.CartRepository
import com.ucb.app.crypto.data.datasource.CryptoRemoteDatasource
import com.ucb.app.crypto.data.repository.CryptoRepositoryImpl
import com.ucb.app.crypto.data.service.CryptoService
import com.ucb.app.crypto.domain.repository.CryptoRepository
import com.ucb.app.firebase.data.datasource.FirebaseManager
import com.ucb.app.github.data.datasource.GitHubRemoteDataSource
import com.ucb.app.github.data.repository.GitHubRepositoryImpl
import com.ucb.app.github.data.service.GitHubService
import com.ucb.app.github.domain.repository.GitHubRepository
import com.ucb.app.movie.data.datasource.MovieRemoteDataSource
import com.ucb.app.movie.data.repository.MovieRepositoryImpl
import com.ucb.app.movie.data.service.MovieService
import com.ucb.app.movie.domain.repository.MovieRepository
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

    // GitHub
    single { GitHubService(get()) }
    single { GitHubRemoteDataSource(get()) }
    single<GitHubRepository> { GitHubRepositoryImpl(get()) }

    // Movie (TMDB)
    single { MovieService(get()) }
    single { MovieRemoteDataSource(get()) }
    single<MovieRepository> { MovieRepositoryImpl(get()) }

    // Crypto
    single<CryptoRemoteDatasource> { CryptoService() }
    single<CryptoRepository> { CryptoRepositoryImpl(get()) }

    // Firebase
    single { FirebaseManager() }

    // Le enseñamos a Koin cómo construir la base de datos principal de Room
    single<AppDatabase> {
        getDatabaseBuilder().build()
    }

    // Obtiene el CartDao directamente de tu AppDatabase (SOLO DEBE ESTAR UNA VEZ)
    single { get<AppDatabase>().cartDao() }

    // Inyecta el DAO en el Repositorio (SOLO DEBE ESTAR UNA VEZ)
    single { CartRepository(get()) }

    // Firebase Remote Config
    single { com.ucb.app.firebase.data.datasource.RemoteConfigManager() }
}

