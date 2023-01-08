package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen

import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

sealed class PlantsListUiState{
    class Start(): PlantsListUiState()
    class DataLoaded(val plants: List<Plant>): PlantsListUiState()
    class NoData(): PlantsListUiState()
    class Error(val errorId: Int): PlantsListUiState()
}
