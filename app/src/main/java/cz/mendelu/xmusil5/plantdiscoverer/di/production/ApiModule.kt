package cz.mendelu.xmusil5.plantdiscoverer.di

import cz.mendelu.xmusil5.plantdiscoverer.communication.UnsplashApi
import org.koin.dsl.module
import retrofit2.Retrofit


val apiModule = module {
    single {
        provideUnsplashApi(get())
    }
}

fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi
        = retrofit.create(UnsplashApi::class.java)