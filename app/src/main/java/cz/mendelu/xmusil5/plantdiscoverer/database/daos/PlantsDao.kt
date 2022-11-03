package cz.mendelu.xmusil5.plantdiscoverer.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.xmusil5.plantdiscoverer.database.entities.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {
    @Query("SELECT * FROM plants")
    fun getAll(): Flow<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :plantId")
    suspend fun getById(plantId: Long): Plant

    @Insert
    suspend fun insert(plant: Plant)
    @Update
    suspend fun update(plant: Plant)
    @Delete
    suspend fun delete(plant: Plant)
}