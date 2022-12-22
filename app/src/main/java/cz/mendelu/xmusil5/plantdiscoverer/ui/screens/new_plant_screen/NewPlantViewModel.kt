package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.MediaStore
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.ImageReckognizer
import kotlinx.coroutines.launch
import java.io.File


class NewPlantViewModel(
    private val plantsDbRepository: IPlantsDbRepository,
    private val imageReckognizer: ImageReckognizer
    ): ViewModel() {

    val newPlantUiState: MutableState<NewPlantUiState> = mutableStateOf(NewPlantUiState.Start())

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
            if (!it.isEmpty()){
                newPlantUiState.value = NewPlantUiState.ImageReckognized(photo, it.first())
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

    fun getCurrentUserLocation(context: Context, onSuccess: (Location) -> Unit){
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
            onSuccess(it)
        }
    }
}