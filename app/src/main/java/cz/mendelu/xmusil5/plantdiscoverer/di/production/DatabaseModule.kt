package cz.mendelu.xmusil5.plantdiscoverer.di

import android.content.Context
import cz.mendelu.xmusil5.PlantDiscovererApplication
import cz.mendelu.xmusil5.plantdiscoverer.database.PlantDiscovererDatabase
import org.koin.dsl.module


val databaseModule = module {
    single {
        providePlantDiscovererDatabase(PlantDiscovererApplication.appContext)
    }
}

fun providePlantDiscovererDatabase(context: Context): PlantDiscovererDatabase{
    return PlantDiscovererDatabase.getDatabase(context)
}