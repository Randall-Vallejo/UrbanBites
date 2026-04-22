package urbanbites.com.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

// Importamos los módulos de la carpeta UCB para que la Demo funcione
import com.ucb.app.di.dataModule as ucbDataModule
import com.ucb.app.di.domainModule as ucbDomainModule
import com.ucb.app.di.presentationModule as ucbPresentationModule

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(getModules())
    }
}

fun getModules() = listOf(
    dataModule,
    domainModule,
    presentationModule,
    ucbDataModule,
    ucbDomainModule,
    ucbPresentationModule
)
