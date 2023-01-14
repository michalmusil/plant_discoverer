package cz.mendelu.xmusil5.plantdiscoverer.di.test

import cz.mendelu.xmusil5.plantdiscoverer.communication.UnsplashApi
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.IUnsplashImagesRepository
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.UnsplashImageRepository
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.UnsplashImageRepositoryMock
import cz.mendelu.xmusil5.plantdiscoverer.database.daos.PlantsDao
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.PlantDbRepositoryLocal
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.PlantDbRepositoryMock
import org.koin.dsl.module

val testRepositoryModule = module {
    single { provideIPlantsDbRepositoryTest() }
    single { provideIUnsplashRemoteRepositoryTest() }
}

fun provideIPlantsDbRepositoryTest(): IPlantsDbRepository
        = PlantDbRepositoryMock()

fun provideIUnsplashRemoteRepositoryTest(): IUnsplashImagesRepository
        = UnsplashImageRepositoryMock()