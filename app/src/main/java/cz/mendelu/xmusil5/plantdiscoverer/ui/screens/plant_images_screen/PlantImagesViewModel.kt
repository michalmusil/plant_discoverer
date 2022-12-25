package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.IUnsplashImagesRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.ImagesRequestResult
import kotlinx.coroutines.launch

class PlantImagesViewModel(private val unsplashImageRepository: IUnsplashImagesRepository): ViewModel() {

    val plantPicturesUiState: MutableState<PlantImagesUiState> = mutableStateOf(PlantImagesUiState.Start())

    fun fetchImages(query: String, page: Int){
        viewModelScope.launch {
            val results = unsplashImageRepository.fetchImages(query = query, page = page)
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

    suspend fun fetchAdditionalImages(query: String, page: Int): CommunicationResult<ImagesRequestResult>{
        return unsplashImageRepository.fetchImages(query, page)
    }
}