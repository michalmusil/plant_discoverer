package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen

import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage

sealed class PlantImagesUiState{
    class Start(): PlantImagesUiState()
    class ImagesLoaded(val images: List<UnsplashImage>): PlantImagesUiState()
    class Error(val errorCode: Int): PlantImagesUiState()
}
