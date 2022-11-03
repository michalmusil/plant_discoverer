package cz.mendelu.xmusil5.plantdiscoverer.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController): INavigationRouter {
    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun toHomeScreen() {
        navController.navigate(Destination.HomeScreen.route)
    }

    override fun toPlantsListScreen() {
        navController.navigate(Destination.PlantsListScreen.route)
    }

    override fun toNewPlantScreen() {
        navController.navigate(Destination.NewPlantScreen.route)
    }

    override fun toPlantDetailScreen(plantId: Long) {
        navController.navigate("${Destination.PlantDetailScreen.route}/${plantId}")
    }

    override fun toPlantEditScreen(plantId: Long) {
        navController.navigate("${Destination.PlantEditScreen.route}/${plantId}")
    }

    override fun toPlantPicturesScreen(plantId: Long) {
        navController.navigate("${Destination.PlantPicturesScreen.route}/${plantId}")
    }

    override fun toMapScreen() {
        navController.navigate(Destination.MapScreen.route)
    }

    override fun toSettingsScreen() {
        navController.navigate(Destination.SettingsScreen.route)
    }
}