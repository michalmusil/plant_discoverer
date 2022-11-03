package cz.mendelu.xmusil5.plantdiscoverer.di

import cz.mendelu.xmusil5.plantdiscoverer.database.PlantDiscovererDatabase
import cz.mendelu.xmusil5.plantdiscoverer.database.daos.PlantsDao
import org.koin.dsl.module

val daoModule = module {
    single {
        providePlantsDao(get())
    }
}

fun providePlantsDao(database: PlantDiscovererDatabase): PlantsDao
    = database.plantsDao()