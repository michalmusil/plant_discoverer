package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
            var bitmapRotated: Bitmap? = null

            // Rotating the image to be upright
            // based on how it's stored in file system - orientation stored in exif tag
            val exif = ExifInterface(uri.path.toString())
            val photoOrientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            val rotationDegrees: Float = when(photoOrientation){
                3 -> 180.0f
                4 -> 180.0f
                5 -> 90.0f
                6 -> 90.0f
                7 -> 270.0f
                8 -> 270.0f
                else -> 0.0f
            }
            val matrix = Matrix().apply {
                postRotate(rotationDegrees)
            }
            bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            newPlantUiState.value = NewPlantUiState.PhotoLoaded(bitmapRotated)

            val photoToDelete = File(uri.path)
            if (photoToDelete.exists()){
                photoToDelete.delete()
            }
        } catch (ex: java.lang.Exception){
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