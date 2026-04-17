package urbanbites.com.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(getModules())
    }
}

fun getModules() = listOf(
    dataModule,
    domainModule,
    presentationModule
)
