package cz.mendelu.xmusil5.plantdiscoverer.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController): INavigationRouter {
    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun toPlantsListScreen() {
        navController.navigate("plants_list")
    }

    override fun toNewPlantScreen() {
        navController.navigate("new_plant")
    }

    override fun toPlantDetailScreen(plantId: Long) {
        navController.navigate("plant_detail/${plantId}")
    }

    override fun toPlantEditScreen(plantId: Long) {
        navController.navigate("edit_plant/${plantId}")
    }

    override fun toPlantPicturesScreen(plantId: Long) {
        navController.navigate("plant_pictures/${plantId}")
    }

    override fun toMapScreen() {
        navController.navigate("map")
    }

    override fun toSettingsScreen() {
        navController.navigate("settings")
    }
}