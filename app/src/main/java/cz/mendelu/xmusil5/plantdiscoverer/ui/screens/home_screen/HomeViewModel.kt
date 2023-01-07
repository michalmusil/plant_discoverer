package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.model.code_models.Month
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
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
                val monthCounts = getMonthlyStatsForYear(it, year)
                val activeYears = getActiveYears(it)

                homeUiState.value = HomeUiState.StatisticsLoaded(
                    totalNumOfPlants = numberOfPlants.toLong(),
                    latestPlant = mostRecentPlant,
                    monthCounts = monthCounts,
                    acitveYears = activeYears
                )
            }
        }
    }

    private fun getMonthlyStatsForYear(plants: List<Plant>, year: Int): HashMap<Month, Double>{
        val monthCounts = hashMapOf<Month, Double>(Month.JANUARY to 0.0, Month.FEBRUARY to 0.0, Month.MARCH to 0.0,
            Month.APRIL to 0.0, Month.MAY to 0.0, Month.JUNE to 0.0, Month.JULY to 0.0, Month.AUGUST to 0.0,
            Month.SEPTEMBER to 0.0, Month.OCTOBER to 0.0, Month.NOVEMBER to 0.0, Month.DECEMBER to 0.0)

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

    // Returns active years descending
    private fun getActiveYears(plants: List<Plant>): List<Int>{
        val activeYears = mutableListOf<Int>()
        plants.forEach {
            val calendar = DateUtils.getDate(it.dateDiscovered)
            val year = calendar.get(Calendar.YEAR)
            if (!activeYears.contains(year)){
                activeYears.add(year)
            }
        }
        return activeYears.sortedDescending()
    }

    // FOR TESTING PURPOUSES
    fun addTestData(){
        val testItems = listOf(
            Plant("Aloe xindl", 1615503600000, "Aloe vera", 0, "aa"),
            Plant("Aloe moe", 1615590000000, "Aloe xera", 0, "not"),
            Plant("Racsavasf", 1619820000000, "aaa", 0, ""),
            Plant("2022", 1659909600000, "aaa", 0, "u"),
            Plant("2022-2", 1659909600000, "aaa", 0, "u"),
            Plant("2022-3", 1659477600000, "aaa", 0, "u"),
            Plant("2022-5", 1663538400000, "aaa", 0, "u"),
            Plant("2022-6", 1663624800000, "aaa", 0, "u"),
            Plant("2022-7", 1666389600000, "aaa", 0, "u")
        )
        viewModelScope.launch {
            testItems.forEach {
                plantsDbRepository.insert(it)
            }
        }
    }

}