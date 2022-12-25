package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.IUnsplashImagesRepository
import kotlinx.coroutines.launch

class PlantImagesViewModel(private val unsplashImageRepository: IUnsplashImagesRepository): ViewModel() {

    val plantPicturesUiState: MutableState<PlantImagesUiState> = mutableStateOf(PlantImagesUiState.Start())

    fun fetchImages(query: String){
        viewModelScope.launch {
            val results = unsplashImageRepository.fetchImages(query)
            when (results){
                is CommunicationResult.Success -> {
                    plantPicturesUiState.value = PlantImagesUiState.ImagesLoaded(images = results.data.results)
                }
                is CommunicationResult.Exception -> {
                    plantPicturesUiState.value = PlantImagesUiState.Error(errorCode = R.string.somethingWentWrong)
                }
                is CommunicationResult.Error -> {
                    plantPicturesUiState.value = PlantImagesUiState.Error(errorCode = R.string.somethingWentWrong)
                }
            }
        }
    }
}