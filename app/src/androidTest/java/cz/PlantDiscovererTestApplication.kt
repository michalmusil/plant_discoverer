package cz

import android.app.Application
import android.content.Context
import cz.mendelu.xmusil5.plantdiscoverer.di.*
import cz.mendelu.xmusil5.plantdiscoverer.di.test.testMlModule
import cz.mendelu.xmusil5.plantdiscoverer.di.test.testRepositoryModule
import cz.mendelu.xmusil5.plantdiscoverer.di.test.testViewModelModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PlantDiscovererTestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@PlantDiscovererTestApplication)
            modules(listOf(
                testMlModule, testRepositoryModule, testViewModelModule
            ))
        }
    }


    companion object{
        lateinit var appContext: Context
            private set
    }
}