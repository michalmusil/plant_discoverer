package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.graphics.Bitmap
import com.google.mlkit.vision.label.ImageLabel

sealed class NewPlantUiState{
    class Start(): NewPlantUiState()
    class PhotoLoaded(val photo: Bitmap): NewPlantUiState()
    class ImageReckognized(val photo: Bitmap, val label: ImageLabel): NewPlantUiState()
    class ImageReckognitionFailed(val photo: Bitmap): NewPlantUiState()
    class NewPlantSaved(val newPlantId: Long): NewPlantUiState()
    class Error(val code: Int): NewPlantUiState()
}
