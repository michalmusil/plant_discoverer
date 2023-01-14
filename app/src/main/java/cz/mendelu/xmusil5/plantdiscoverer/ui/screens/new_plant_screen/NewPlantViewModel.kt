package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.ml.ImageRecognizer
import cz.mendelu.xmusil5.plantdiscoverer.ml.ImageRecognizing
import kotlinx.coroutines.launch


class NewPlantViewModel(
    private val plantsDbRepository: IPlantsDbRepository,
    private val imageReckognizer: ImageRecognizing
    ): ViewModel() {

    val newPlantUiState: MutableState<NewPlantUiState> = mutableStateOf(NewPlantUiState.Start())

    fun getImageFromUri(context: Context, uriString: String){
        val image = imageReckognizer.retriveImageFromUri(context, uriString)
        if (image != null){
            newPlantUiState.value = NewPlantUiState.PhotoLoaded(image)
        } else{
            newPlantUiState.value = NewPlantUiState.Error(R.string.somethingWentWrong)
        }
    }

    fun reckognizePhoto(photo: Bitmap){
        viewModelScope.launch {
            imageReckognizer.processImage(photo, onFinishedListener = {
                if (it != null && it.labels.isNotEmpty()){
                    newPlantUiState.value = NewPlantUiState.ImageReckognized(photo, it)
                } else {
                    newPlantUiState.value = NewPlantUiState.ImageReckognitionFailed(photo)
                }
            })
        }
    }

    fun saveNewPlant(plant: Plant, completion: (Long) -> Unit){
        var resultingId: Long = -1L
        viewModelScope.launch {
            resultingId = plantsDbRepository.insert(plant)
        }.invokeOnCompletion {
            completion(resultingId)
        }
    }

    fun getCurrentUserLocation(context: Context, onSuccess: (Location) -> Unit){
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
                it?.let{
                    onSuccess(it)
                }
            }
    }
}