package cz.mendelu.xmusil5.plantdiscoverer.model.database_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "describtion")
    var describtion: String? = null

    @ColumnInfo(name = "picture")
    var photo: ByteArray? = null

    @ColumnInfo(name = "describtion")
    var latitude: Double? = null

    @ColumnInfo(name = "describtion")
    var longitude: Double? = null
}
