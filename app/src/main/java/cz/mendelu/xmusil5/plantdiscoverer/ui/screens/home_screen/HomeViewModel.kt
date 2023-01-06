package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(private val plantsDbRepository: IPlantsDbRepository): ViewModel() {

    val homeUiState: MutableState<HomeUiState> = mutableStateOf(HomeUiState.Start())

    fun fetchStatistics(){
        viewModelScope.launch {
            combine(
                plantsDbRepository.getLatestDiscoveredPlant(),
                plantsDbRepository.getNumberOfDiscoveredPlants()
            ) { latestPlant, numberOfPlants ->
                homeUiState.value = HomeUiState.StatisticsLoaded(totalNumOfPlants = numberOfPlants, latestPlant = latestPlant)
            }.collect()
        }

    }
}