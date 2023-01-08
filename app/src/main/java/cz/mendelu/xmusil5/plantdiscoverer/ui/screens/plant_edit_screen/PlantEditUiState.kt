package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen

import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

sealed class PlantEditUiState {
    class Start(): PlantEditUiState()
    class PlantLoaded(val plant: Plant): PlantEditUiState()
    class Error(val errorCode: Int): PlantEditUiState()
}
