package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen

import cz.mendelu.xmusil5.plantdiscoverer.database.entities.Plant

sealed class PlantsListUiState(){
    class Start(): PlantsListUiState()
    class DataLoaded(plants: List<Plant>): PlantsListUiState()
    class Error(errorId: String): PlantsListUiState()
}
