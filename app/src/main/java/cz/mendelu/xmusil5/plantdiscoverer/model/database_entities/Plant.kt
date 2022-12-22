package cz.mendelu.xmusil5.plantdiscoverer.model.database_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils

@Entity(tableName = "plants")
data class Plant(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "date_discovered")
    var dateDiscovered: Long,
    @ColumnInfo(name = "original_match")
    var originalMatch: String,
    @ColumnInfo(name = "original_certainty")
    var originalCertainty: Int,
    @ColumnInfo(name = "imageQuery")
    var imageQuery: String
): java.io.Serializable, ClusterItem{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "picture")
    var photo: ByteArray? = null

    @ColumnInfo(name = "latitude")
    var latitude: Double? = null

    @ColumnInfo(name = "longitude")
    var longitude: Double? = null




    override fun getPosition(): LatLng {
        return LatLng(
            latitude ?: 0.0,
            longitude ?: 0.0
        )
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getSnippet(): String? {
        return DateUtils.getDateString(dateDiscovered)
    }
}
