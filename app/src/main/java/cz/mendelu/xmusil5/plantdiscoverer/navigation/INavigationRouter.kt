package cz.mendelu.xmusil5.plantdiscoverer.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()

    fun toPlantsListScreen()
    fun toNewPlantScreen()
    fun toPlantDetailScreen(plantId: Long)
    fun toPlantEditScreen(plantId: Long)
    fun toPlantPicturesScreen(plantId: Long)
    fun toMapScreen()
    fun toSettingsScreen()
}