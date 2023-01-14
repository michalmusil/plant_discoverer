package cz.mendelu.xmusil5.plantdiscoverer.di

import android.content.Context
import cz.mendelu.xmusil5.plantdiscoverer.utils.Constants
import cz.mendelu.xmusil5.plantdiscoverer.ml.ImageRecognizer
import cz.mendelu.xmusil5.plantdiscoverer.ml.ImageRecognizing
import org.koin.dsl.module

val mlModule = module {
    single {
        provideImageReckognizer(get())
    }
}

fun provideImageReckognizer(context: Context): ImageRecognizing {
    return ImageRecognizer(Constants.ML_MODEL_PATH, context)
}