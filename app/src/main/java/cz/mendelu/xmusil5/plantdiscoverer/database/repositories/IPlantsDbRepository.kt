package cz.mendelu.xmusil5.plantdiscoverer.database.repositories

import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import kotlinx.coroutines.flow.Flow

interface IPlantsDbRepository {
    fun getAll(): Flow<List<Plant>>

    fun getAllWithLocation(): Flow<List<Plant>>

    suspend fun getById(plantId: Long): Flow<Plant?>

    fun getLatestDiscoveredPlant(): Flow<Plant?>

    fun getNumberOfDiscoveredPlants(): Flow<Long>

    suspend fun insert(plant: Plant): Long
    suspend fun update(plant: Plant)
    suspend fun delete(plant: Plant)
    suspend fun deleteById(plantId: Long)
}