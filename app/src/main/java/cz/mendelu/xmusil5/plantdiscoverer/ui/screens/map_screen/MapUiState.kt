package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen

import android.location.Location
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

sealed class MapUiState{
    class Start(): MapUiState()
    class PlantsLoaded(val plants: List<Plant>): MapUiState()
    class LocationResults(val location: Location): MapUiState()
    class PermissionsDenied(): MapUiState()
    class Error(val errorCode: Int): MapUiState()
}
