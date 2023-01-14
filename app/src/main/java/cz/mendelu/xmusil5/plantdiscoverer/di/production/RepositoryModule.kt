package cz.mendelu.xmusil5.plantdiscoverer.di

import cz.mendelu.xmusil5.plantdiscoverer.communication.UnsplashApi
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.IUnsplashImagesRepository
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.UnsplashImageRepository
import cz.mendelu.xmusil5.plantdiscoverer.database.daos.PlantsDao
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.PlantDbRepositoryLocal
import org.koin.dsl.module

val repositoryModule = module {
    single { provideIPlantsDbRepository(get()) }
    single { provideIUnsplashRemoteRepository(get()) }
}

fun provideIPlantsDbRepository(plantsDao: PlantsDao): IPlantsDbRepository
    = PlantDbRepositoryLocal(plantsDao)

fun provideIUnsplashRemoteRepository(unsplashApi: UnsplashApi): IUnsplashImagesRepository
        = UnsplashImageRepository(unsplashApi)