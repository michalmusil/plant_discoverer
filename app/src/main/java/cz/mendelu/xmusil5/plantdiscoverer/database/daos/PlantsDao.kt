package cz.mendelu.xmusil5.plantdiscoverer.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {
    @Query("SELECT * FROM plants ORDER BY date_discovered DESC")
    fun getAll(): Flow<List<Plant>>

    @Query("SELECT * FROM plants WHERE latitude NOT NULL AND longitude NOT NULL")
    fun getAllWithLocation(): Flow<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :plantId")
    fun getById(plantId: Long): Flow<Plant?>

    // Statistics
    @Query("SELECT * FROM plants WHERE date_discovered = (SELECT MAX(date_discovered) FROM plants) LIMIT 1")
    fun getLatestDiscoveredPlant(): Flow<Plant?>

    @Query("SELECT COUNT(*) FROM plants")
    fun getNumberOfDiscoveredPlants(): Flow<Long>


    @Insert
    suspend fun insert(plant: Plant): Long
    @Update
    suspend fun update(plant: Plant)
    @Delete
    suspend fun delete(plant: Plant)
}