package cz.mendelu.xmusil5.plantdiscoverer.di

import android.content.Context
import cz.mendelu.xmusil5.plantdiscoverer.utils.Constants
import cz.mendelu.xmusil5.plantdiscoverer.utils.ImageReckognizer
import org.koin.dsl.module

val mlModule = module {
    single {
        provideImageReckognizer(get())
    }
}

fun provideImageReckognizer(context: Context): ImageReckognizer{
    return ImageReckognizer(Constants.ML_MODEL_PATH, context)
}