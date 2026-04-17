package com.ucb.app.di

import com.ucb.app.github.domain.usecase.GetGitHubUserUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetGitHubUserUseCase(get()) }
}
