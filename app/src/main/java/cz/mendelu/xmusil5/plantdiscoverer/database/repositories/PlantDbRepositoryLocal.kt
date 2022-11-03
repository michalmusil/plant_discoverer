package cz.mendelu.xmusil5.plantdiscoverer.database.repositories

import cz.mendelu.xmusil5.plantdiscoverer.database.daos.PlantsDao
import cz.mendelu.xmusil5.plantdiscoverer.database.entities.Plant
import kotlinx.coroutines.flow.Flow

class PlantDbRepositoryLocal(private val plantsDao: PlantsDao): IPlantsDbRepository {
    override fun getAll(): Flow<List<Plant>> {
        return plantsDao.getAll()
    }

    override suspend fun getById(plantId: Long): Plant {
        return plantsDao.getById(plantId)
    }

    override suspend fun insert(plant: Plant) {
        plantsDao.insert(plant)
    }

    override suspend fun update(plant: Plant) {
        plantsDao.update(plant)
    }

    override suspend fun delete(plant: Plant) {
        plantsDao.delete(plant)
    }
}