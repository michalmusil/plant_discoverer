package cz.mendelu.xmusil5.plantdiscoverer.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

class LanguageUtils(private val context: Context) {
    enum class Language(val code: String, val originName: String){
        ENGLISH("en", "🇺🇸 English"),
        SLOVAK("sk", "🇸🇰 Slovenčina"),
        CZECH("cs", "🇨🇿 Čeština");

        companion object {
            fun getByCode(code: String): Language?{
                return Language.values().filter {
                    it.code == code
                }.firstOrNull()
            }

            fun getByCodeDefaultEnglish(code: String): Language{
                return Language.values().filter {
                    it.code == code
                }.firstOrNull() ?: ENGLISH
            }
        }
    }
    enum class DateFormat(val dateformat: SimpleDateFormat){
        DAY_MONTH_YEAR_DOTS(SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)),
        YEAR_MONTH_DAY_SLASHES(SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH));

        companion object {
            fun getByLanguage(language: Language): DateFormat{
                when (language) {
                    Language.SLOVAK -> {
                        return DAY_MONTH_YEAR_DOTS
                    }
                    Language.CZECH -> {
                        return DAY_MONTH_YEAR_DOTS
                    }
                    else -> {
                        return YEAR_MONTH_DAY_SLASHES
                    }
                }
            }
        }
    }

    suspend fun getAppLanguage(): Language{
        val preferences = context.settingsDataStore.data.first()
        val languageCode = preferences[stringPreferencesKey(Constants.APP_LANGUAGE_KEY)]
        if (languageCode != null){
            val appLanguage = Language.getByCode(languageCode)
            return appLanguage ?: Language.ENGLISH
        } else {
            val languageCode = Locale.getDefault().language
            val newAppLanguage = Language.getByCode(languageCode) ?: Language.ENGLISH
            return newAppLanguage
        }
    }

    suspend fun setAppLanguage(language: Language){
        context.settingsDataStore.edit {
            it[stringPreferencesKey(Constants.APP_LANGUAGE_KEY)] = language.code
            updateAppLanguageInUi(language)
        }
    }

    fun updateAppLanguageInUi(language: Language){
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    suspend fun getDateFormat(): DateFormat {
        val appLanguage = getAppLanguage()
        when (appLanguage) {
            Language.SLOVAK -> {
                return DateFormat.DAY_MONTH_YEAR_DOTS
            }
            Language.CZECH -> {
                return DateFormat.DAY_MONTH_YEAR_DOTS
            }
            else -> {
                return DateFormat.YEAR_MONTH_DAY_SLASHES
            }
        }
    }
}
