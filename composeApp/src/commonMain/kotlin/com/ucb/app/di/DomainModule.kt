package com.ucb.app.di

import com.ucb.app.login.domain.usecase.DoLoginUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { DoLoginUseCase(get()) }
}
