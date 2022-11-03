package cz.mendelu.xmusil5.plantdiscoverer.database.repositories

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.xmusil5.plantdiscoverer.database.entities.Plant
import kotlinx.coroutines.flow.Flow

interface IPlantsDbRepository {
    fun getAll(): Flow<List<Plant>>

    suspend fun getById(plantId: Long): Plant

    suspend fun insert(plant: Plant)
    suspend fun update(plant: Plant)
    suspend fun delete(plant: Plant)
}