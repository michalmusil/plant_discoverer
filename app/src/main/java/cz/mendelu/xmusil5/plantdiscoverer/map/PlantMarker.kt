package cz.mendelu.xmusil5.plantdiscoverer.map

import com.google.android.gms.maps.model.Marker
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

data class PlantMarker(
    val plant: Plant,
    val marker: Marker
)
