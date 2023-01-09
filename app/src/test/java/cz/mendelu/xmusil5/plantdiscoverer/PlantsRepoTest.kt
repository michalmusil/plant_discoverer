package cz.mendelu.xmusil5.plantdiscoverer

import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.communication.repositories.UnsplashImageRepositoryMock
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.IPlantsDbRepository
import cz.mendelu.xmusil5.plantdiscoverer.database.repositories.PlantDbRepositoryMock
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class PlantsRepoTest {

    val plantsRepo: IPlantsDbRepository = PlantDbRepositoryMock()

    val plantForTesiting =  Plant(name = "Test plant",
        dateDiscovered = DateUtils.getCurrentUnixTime(),
        originalMatch = "Test",
        originalCertainty = 20,
        imageQuery = "tst").apply {
            id = 4L
    }

    @Test
    fun getAll(){
        runBlocking {
            val plants = plantsRepo.getAll().first()
            assertEquals(plants.size, PlantDbRepositoryMock.mockPlants.size)
            assertEquals(plants.first(), PlantDbRepositoryMock.mockPlants.first())
        }
    }

    @Test
    fun getAllWithLocation(){
        runBlocking {
            val fromMock = PlantDbRepositoryMock.mockPlants.filter { it.latitude != null && it.longitude != null }
            val fromRepo = plantsRepo.getAllWithLocation().first()
            assertEquals(fromRepo.size, fromMock.size)
        }
    }

    @Test
    fun getById(){
        runBlocking {
            val id = 2L
            val fromMock = PlantDbRepositoryMock.mockPlants.firstOrNull { it.id == id }
            val fromRepo = plantsRepo.getById(id).first()
            assertEquals(fromRepo, fromMock)
        }
    }

    @Test
    fun getLatestDiscovered(){
        runBlocking {
            val fromMock = PlantDbRepositoryMock.mockPlants.firstOrNull{
                it.dateDiscovered == PlantDbRepositoryMock.mockPlants.maxOf { it.dateDiscovered }
            }
            val fromRepo = plantsRepo.getLatestDiscoveredPlant().first()
            assertEquals(fromRepo, fromMock)
        }
    }

    @Test
    fun getNumberOfPlants(){
        runBlocking {
            val fromMock = PlantDbRepositoryMock.mockPlants.size.toLong()
            val fromRepo = plantsRepo.getNumberOfDiscoveredPlants().first()
            assertEquals(fromRepo, fromMock)
        }
    }
}