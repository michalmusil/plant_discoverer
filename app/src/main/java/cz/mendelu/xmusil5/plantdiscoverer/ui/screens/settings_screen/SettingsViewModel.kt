package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.utils.LanguageUtils
import kotlinx.coroutines.launch

class SettingsViewModel(private val languageUtils: LanguageUtils): ViewModel() {

    val settingsUiState: MutableState<SettingsUiState> = mutableStateOf(SettingsUiState.Start())


    fun setAppLanguage(language: LanguageUtils.Language, completion: () -> Unit){
        viewModelScope.launch {
            languageUtils.setAppLanguage(language)
        }.invokeOnCompletion {
            completion()
        }
    }
}