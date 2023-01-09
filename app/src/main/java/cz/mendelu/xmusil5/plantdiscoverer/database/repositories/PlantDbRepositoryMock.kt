package cz.mendelu.xmusil5.plantdiscoverer.database.repositories

import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class PlantDbRepositoryMock: IPlantsDbRepository {

    companion object{
        private val plants = listOf(
            Plant(name = "Aloe vera",
                dateDiscovered = DateUtils.getCurrentUnixTime(),
                originalMatch = "Aloe vera",
                originalCertainty = 55,
                imageQuery = "Aloe"),
            Plant(name = "Aloe ferox",
                dateDiscovered = DateUtils.getCurrentUnixTime(),
                originalMatch = "Aloe vera",
                originalCertainty = 15,
                imageQuery = "Ferox"),
            Plant(name = "Bilomea proxia",
                dateDiscovered = DateUtils.getCurrentUnixTime(),
                originalMatch = "-",
                originalCertainty = 0,
                imageQuery = "Bilomea")
        )

        private var idCounter = 1L
        val mockPlants: MutableList<Plant> = plants.map {
            it.id = idCounter
            idCounter += 1L
            if (idCounter == 2L) { it.latitude = 73.2; it.longitude = 20.3 }
            it
        }.toMutableList()

    }
    override fun getAll(): Flow<List<Plant>> {
        return flow { emit(mockPlants) }
    }

    override fun getAllWithLocation(): Flow<List<Plant>> {
        return flow { emit(mockPlants.filter { it.latitude != null && it.longitude != null }) }
    }

    override suspend fun getById(plantId: Long): Flow<Plant?> {
        return flow { emit(mockPlants.filter { it.id == plantId }.firstOrNull() ?: mockPlants.first()) }
    }

    override fun getLatestDiscoveredPlant(): Flow<Plant?> {
        return flow {
            emit(
                mockPlants.firstOrNull{
                    it.dateDiscovered == mockPlants.maxOf { it.dateDiscovered }
                }
            )
        }
    }

    override fun getNumberOfDiscoveredPlants(): Flow<Long> {
        return flow { emit(mockPlants.size.toLong()) }
    }

    override suspend fun insert(plant: Plant): Long {
        plant.id = mockPlants.size.toLong() + 1
        mockPlants.add(plant)
        return mockPlants.size.toLong()
    }

    override suspend fun update(plant: Plant) {
        mockPlants.removeIf { it.id == plant.id }
        mockPlants.add(plant)
    }

    override suspend fun delete(plant: Plant) {
        mockPlants.remove(plant)
    }

    override suspend fun deleteById(plantId: Long) {
        mockPlants.removeIf { it.id == plantId }
    }
}