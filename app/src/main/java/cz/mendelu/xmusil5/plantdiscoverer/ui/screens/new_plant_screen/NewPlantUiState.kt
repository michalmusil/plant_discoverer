package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.graphics.Bitmap

sealed class NewPlantUiState{
    class Start(): NewPlantUiState()
    class PhotoLoaded(val photo: Bitmap): NewPlantUiState()
    class NewPlantSaved(val newPlantId: Long): NewPlantUiState()
    class Error(val code: Int): NewPlantUiState()
}
