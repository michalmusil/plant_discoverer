package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen

import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

sealed class PlantDetailUiState{
    class Start(): PlantDetailUiState()
    class PlantLoaded(val plant: Plant): PlantDetailUiState()
    class Error(val errorCode: Int): PlantDetailUiState()
}
