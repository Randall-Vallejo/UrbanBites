package urbanbites.com

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import com.ucb.app.di.initKoin // Usamos el original de UCB

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
        }
    }
}
