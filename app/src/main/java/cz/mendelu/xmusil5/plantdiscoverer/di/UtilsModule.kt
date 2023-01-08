package cz.mendelu.xmusil5.plantdiscoverer.di

import android.content.Context
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.LanguageUtils
import org.koin.dsl.module

val utilsModule = module {
    single {
        provideLanguageUtils(get())
    }

}

fun provideLanguageUtils(context: Context) = LanguageUtils(context)