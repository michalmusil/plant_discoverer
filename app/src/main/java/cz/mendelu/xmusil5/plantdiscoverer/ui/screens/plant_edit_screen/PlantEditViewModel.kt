package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import kotlinx.coroutines.launch

class PlantEditViewModel(private val plantsDbRepository: IPlantsDbRepository): ViewModel() {

    val plantEditUiState: MutableState<PlantEditUiState> = mutableStateOf(PlantEditUiState.Start())

    fun loadPlant(plantId: Long){
        viewModelScope.launch {
            plantsDbRepository.getById(plantId).collect{
                it?.let {
                    plantEditUiState.value = PlantEditUiState.PlantLoaded(plant = it)
                }
            }
        }
    }

    fun saveChangesToPlant(plant: Plant){
        viewModelScope.launch {
            plantsDbRepository.update(plant)
        }.invokeOnCompletion {
            plantEditUiState.value = PlantEditUiState.ChangesSaved()
        }
    }
}