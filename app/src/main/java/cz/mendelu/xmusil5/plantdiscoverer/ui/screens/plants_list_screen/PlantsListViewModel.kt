package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlantsListViewModel(private val plantsDbRepository: IPlantsDbRepository): ViewModel() {

    val plantsListUiState: MutableState<PlantsListUiState> = mutableStateOf(PlantsListUiState.Start())

    fun loadPlants(){
        viewModelScope.launch {
            plantsDbRepository.getAll().collect{
                if (it.size > 0) {
                    plantsListUiState.value = PlantsListUiState.DataLoaded(it)
                } else {
                    plantsListUiState.value = PlantsListUiState.NoData()
                }
            }
        }

    }
}