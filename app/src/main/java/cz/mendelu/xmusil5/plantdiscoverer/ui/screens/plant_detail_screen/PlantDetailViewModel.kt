package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlantDetailViewModel(private val plantsDbRepository: IPlantsDbRepository): ViewModel() {

    val plantDetailUiState: MutableState<PlantDetailUiState> = mutableStateOf(PlantDetailUiState.Start())

    fun loadPlant(plantId: Long){
        viewModelScope.launch {
            plantsDbRepository.getById(plantId).collect{
                if (it == null){
                    plantDetailUiState.value = PlantDetailUiState.Error(R.string.plantNotFound)
                } else {
                    plantDetailUiState.value = PlantDetailUiState.PlantLoaded(it)
                }
            }
        }
    }

    fun deletePlant(plantId: Long, completion: () -> Unit){
        viewModelScope.launch {
            plantsDbRepository.deleteById(plantId)
        }.invokeOnCompletion {
            completion()
        }
    }
}