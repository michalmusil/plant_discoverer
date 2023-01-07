package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import cz.mendelu.xmusil5.plantdiscoverer.model.code_models.Month
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

sealed class HomeUiState{
    class Start(): HomeUiState()
    class StatisticsLoaded(
        val totalNumOfPlants: Long,
        val latestPlant: Plant?,
        val monthCounts: HashMap<Month, Int>): HomeUiState()
    class Error(val errorCode: Int): HomeUiState()
}
