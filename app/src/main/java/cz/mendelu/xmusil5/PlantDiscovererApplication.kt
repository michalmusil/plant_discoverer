package cz.mendelu.xmusil5

import android.app.Application
import android.content.Context
import cz.mendelu.xmusil5.plantdiscoverer.di.daoModule
import cz.mendelu.xmusil5.plantdiscoverer.di.databaseModule
import cz.mendelu.xmusil5.plantdiscoverer.di.repositoryModule
import cz.mendelu.xmusil5.plantdiscoverer.di.viewModelModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PlantDiscovererApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@PlantDiscovererApplication)
            modules(listOf(
                databaseModule, daoModule, repositoryModule, viewModelModule
            ))
        }
    }

    companion object{
        lateinit var appContext: Context
            private set
    }
}