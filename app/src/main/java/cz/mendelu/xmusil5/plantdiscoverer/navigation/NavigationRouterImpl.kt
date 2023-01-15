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

    override fun toNewPlantScreen(takenPhotoUri: String) {
        val route = "${Destination.NewPlantScreen.route}?takenPhotoUri=${takenPhotoUri}"
        navController.navigate(route)
    }

    override fun toPlantDetailScreen(plantId: Long) {
        navController.navigate("${Destination.PlantDetailScreen.route}/${plantId}")
    }

    override fun toPlantEditScreen(plantId: Long) {
        navController.navigate("${Destination.PlantEditScreen.route}/${plantId}")
    }

    override fun toPlantImagesScreen(query: String) {
        navController.navigate("${Destination.PlantImagesScreen.route}/${query}")
    }

    override fun toMapScreen() {
        navController.navigate(Destination.MapScreen.route)
    }

    override fun toSettingsScreen() {
        navController.navigate(Destination.SettingsScreen.route)
    }

    override fun toCameraScreen() {
        navController.navigate(Destination.CameraScreen.route)
    }
}