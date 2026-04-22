package com.ucb.app.di

import com.ucb.app.crypto.domain.usecase.GetCryptosUseCase
import com.ucb.app.dollar.domain.usecase.CreateDollarUseCase
import com.ucb.app.dollar.domain.usecase.GetDollarListUsecase
import com.ucb.app.github.domain.usecase.GetGitHubUserUseCase
import com.ucb.app.movie.domain.usecase.GetPopularMoviesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetGitHubUserUseCase(get()) }
    factory { GetPopularMoviesUseCase(get()) }
    factory { GetCryptosUseCase(get()) }
    factory { CreateDollarUseCase(get()) }
    factory { GetDollarListUsecase(get()) }
}
