package cz.mendelu.xmusil5.plantdiscoverer.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

class PlantDiscovererMapRenderer(var context: Context,
                        var map: GoogleMap,
                        var clusterManager: ClusterManager<Plant>
)
    : DefaultClusterRenderer<Plant>(context, map, clusterManager) {

    override fun shouldRenderAsCluster(cluster: Cluster<Plant>): Boolean {
        return cluster.size > 4
    }

    override fun onBeforeClusterItemRendered(item: Plant, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)

        markerOptions.apply {
            icon(
                BitmapDescriptorFactory.fromBitmap(
                    MarkerUtils.createCustomMarkerFromLayout(context, item, false)
                )
            )
        }
    }
}