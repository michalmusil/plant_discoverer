package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen

import android.content.Context
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MapViewModel(private val plantsDbRepository: IPlantsDbRepository): ViewModel() {

    val mapUiState: MutableState<MapUiState> = mutableStateOf(MapUiState.Start())

    fun loadPlantsWithLocation(){
        viewModelScope.launch {
            plantsDbRepository.getAllWithLocation().collect{
                mapUiState.value = MapUiState.PlantsLoaded(it)
            }
        }
    }

    fun getCurrentUserLocation(context: Context, onSuccess: (Location) -> Unit){
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        locationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener {
                onSuccess(it)
            }
    }
}