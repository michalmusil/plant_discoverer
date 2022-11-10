package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabelerOptionsBase
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.ImageReckognizer
import kotlinx.coroutines.launch
import java.io.File

class NewPlantViewModel(private val plantsDbRepository: IPlantsDbRepository): ViewModel() {

    val newPlantUiState: MutableState<NewPlantUiState> = mutableStateOf(NewPlantUiState.Start())
    val imageReckognizer = ImageReckognizer()

    fun getImageFromUri(context: Context, uriString: String){
        try {
            val uri = Uri.parse(uriString)
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

            val rotationMatrix = Matrix()
            rotationMatrix.postRotate(90f)
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotationMatrix, true);
            newPlantUiState.value = NewPlantUiState.PhotoLoaded(rotatedBitmap)

            val photoToDelete = File(uri.path)
            if (photoToDelete.exists()){
                photoToDelete.delete()
            }
        } catch (ex: java.lang.Exception){
            newPlantUiState.value = NewPlantUiState.Error(R.string.somethingWentWrong)
        }
    }

    fun reckognizePhoto(photo: Bitmap){
        imageReckognizer.processImage(photo, onFinishedListener = {
            if (it != null){
                newPlantUiState.value = NewPlantUiState.ImageReckognized(photo, it)
            } else {
                newPlantUiState.value = NewPlantUiState.ImageReckognitionFailed(photo)
            }
        })
    }

    fun saveNewPlant(plant: Plant){
        var resultingId: Long = -1L
        viewModelScope.launch {
            resultingId = plantsDbRepository.insert(plant)
        }.invokeOnCompletion {
            newPlantUiState.value = NewPlantUiState.NewPlantSaved(resultingId)
        }
    }
}