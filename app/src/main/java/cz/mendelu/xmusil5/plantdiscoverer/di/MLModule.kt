package cz.mendelu.xmusil5.plantdiscoverer.di

import cz.mendelu.xmusil5.plantdiscoverer.utils.ImageReckognizer
import org.koin.dsl.module

val mlModule = module {
    single {
        provideImageReckognizer()
    }
}

fun provideImageReckognizer(): ImageReckognizer{
    return ImageReckognizer("plants_model_V1_3.tflite")
}