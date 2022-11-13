package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen

import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

sealed class MapUiState{
    class Start(): MapUiState()
    class PlantsLoaded(val plants: List<Plant>): MapUiState()
    class PermissionsDenied(): MapUiState()
}
