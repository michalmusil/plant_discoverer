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
                //plantsListUiState.value = PlantsListUiState.DataLoaded(it)

                plantsListUiState.value = PlantsListUiState.DataLoaded(
                    listOf(
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("ajksnfgl;qjierf", 123123L, "asdf", 20, "lame"),
                        Plant("adjfkn", 123123L, "asdf", 20, "lame"),
                        Plant("vnvkjfn", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Assssdfaefqwerqwefcfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("adsfadsf", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                        Plant("asdfasefdqwefqasdv", 123123L, "asdf", 20, "lame"),
                        Plant("Acfd", 123123L, "asdf", 20, "lame"),
                    )
                )
            }
        }

    }
}