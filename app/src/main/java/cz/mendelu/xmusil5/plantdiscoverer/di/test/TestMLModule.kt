package cz.mendelu.xmusil5.plantdiscoverer.di.test

import android.content.Context
import cz.mendelu.xmusil5.plantdiscoverer.utils.Constants
import cz.mendelu.xmusil5.plantdiscoverer.ml.ImageRecognizer
import cz.mendelu.xmusil5.plantdiscoverer.ml.ImageRecognizerMock
import cz.mendelu.xmusil5.plantdiscoverer.ml.ImageRecognizing
import org.koin.dsl.module

val testMlModule = module {
    single {
        provideImageReckognizerTest()
    }
}

fun provideImageReckognizerTest(): ImageRecognizing {
    return ImageRecognizerMock()
}