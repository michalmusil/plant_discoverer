package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.code_models.Month
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(private val plantsDbRepository: IPlantsDbRepository): ViewModel() {

    val homeUiState: MutableState<HomeUiState> = mutableStateOf(HomeUiState.Start())

    fun fetchStatistics(year: Int){
        viewModelScope.launch {
            plantsDbRepository.getAll().collect{
                val numberOfPlants = it.size
                val latestDate = 0L
                var mostRecentPlant: Plant? = null
                it.forEach {
                    if (it.dateDiscovered > latestDate){
                        mostRecentPlant = it
                    }
                }
                val monthCounts = transformToMonths(it, year)

                homeUiState.value = HomeUiState.StatisticsLoaded(
                    totalNumOfPlants = numberOfPlants.toLong(),
                    latestPlant = mostRecentPlant,
                    monthCounts = monthCounts
                )
            }
        }
    }

    private fun transformToMonths(plants: List<Plant>, year: Int): HashMap<Month, Double>{
        val monthCounts = hashMapOf<Month, Double>(Month.JANUARY to 0.0, Month.FEBRUARY to 0.0, Month.MARCH to 13.0,
            Month.APRIL to 12.0, Month.MAY to 6.0, Month.JUNE to 18.0, Month.JULY to 6.0, Month.AUGUST to 0.0,
            Month.SEPTEMBER to 1.0, Month.OCTOBER to 0.0, Month.NOVEMBER to 4.0, Month.DECEMBER to 2.0)

        plants.forEach {
            val calendar = DateUtils.getDate(it.dateDiscovered)
            if (calendar.get(Calendar.YEAR) == year){
                val monthOfCalendar = calendar.get(Calendar.MONTH) + 1
                val monthProprietary = Month.getByOrder(monthOfCalendar)
                monthProprietary?.let {
                    val currentCount = monthCounts.get(it)
                    monthCounts.replace(it, currentCount!!+1)
                }
            }
        }
        return monthCounts
    }


}