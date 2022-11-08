package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel(): ViewModel() {

    val settingsUiState: MutableState<SettingsUiState> = mutableStateOf(SettingsUiState.Start())
}