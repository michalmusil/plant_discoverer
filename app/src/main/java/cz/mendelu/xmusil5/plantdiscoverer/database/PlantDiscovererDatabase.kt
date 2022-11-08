package cz.mendelu.xmusil5.plantdiscoverer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.xmusil5.plantdiscoverer.database.daos.PlantsDao
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant

@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class PlantDiscovererDatabase : RoomDatabase() {

    abstract fun plantsDao(): PlantsDao

    companion object {

        private var INSTANCE: PlantDiscovererDatabase? = null

        fun getDatabase(context: Context): PlantDiscovererDatabase {
            if (INSTANCE == null) {
                synchronized(PlantDiscovererDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PlantDiscovererDatabase::class.java, "Plant_discoverer_database"
                        ).fallbackToDestructiveMigration().build() // TODO Remove the fallbackToDestructiveMigration in production !!!
                    }
                }
            }
            return INSTANCE!!
        }
    }
}