package com.ucb.app.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(getModules())
    }
}

fun getModules() = listOf(
    platformModule, // Agregado para resolver dependencias de plataforma
    dataModule,
    domainModule,
    presentationModule
)
