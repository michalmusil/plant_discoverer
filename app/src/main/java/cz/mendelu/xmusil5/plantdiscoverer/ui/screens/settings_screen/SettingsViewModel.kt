package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.xmusil5.plantdiscoverer.utils.ImageReckognizer
import cz.mendelu.xmusil5.plantdiscoverer.utils.LanguageUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.settingsDataStore
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val languageUtils: LanguageUtils,
    private val imageReckognizer: ImageReckognizer
    ): ViewModel() {

    val settingsUiState: MutableState<SettingsUiState> = mutableStateOf(SettingsUiState.Start())


    fun setAppLanguage(language: LanguageUtils.Language, completion: () -> Unit){
        viewModelScope.launch {
            languageUtils.setAppLanguage(language)
        }.invokeOnCompletion {
            completion()
        }
    }

    suspend fun setNewConfidenceTreshold(treshold: Int){
        if (treshold in 0..100){
            viewModelScope.launch {
                imageReckognizer.setConfidenceTreshold(treshold.toFloat())
            }
        }
    }
}