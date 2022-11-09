package cz.mendelu.xmusil5.plantdiscoverer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.camera_screen.CameraScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen.HomeScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen.MapScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen.NewPlantScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen.PlantDetailScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen.PlantEditScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_pictures_screen.PlantPicturesScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen.PlantsListScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Destination.PlantsListScreen.route) {
            PlantsListScreen(navigation)
        }

        composable(Destination.HomeScreen.route) {
            HomeScreen(navigation)
        }

        composable(Destination.PlantDetailScreen.route + "/{plantId}",
            arguments = listOf(
                navArgument("plantId"){
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )) {
            val plantId = it.arguments?.getLong("plantId")
            PlantDetailScreen(navigation = navigation, plantId = plantId!!)
        }

        composable(Destination.PlantEditScreen.route + "/{plantId}",
            arguments = listOf(
                navArgument("plantId"){
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )) {
            val plantId = it.arguments?.getLong("plantId")
            PlantEditScreen(navigation = navigation, plantId = plantId!!)
        }

        composable(Destination.NewPlantScreen.route + "?takenPhotoUri={takenPhotoUri}",
            arguments = listOf(
                navArgument("takenPhotoUri"){
                    type = NavType.StringType
                }
            )) {
            val takenPhotoUri = it.arguments?.getString("takenPhotoUri")
            NewPlantScreen(navigation = navigation, takenPhotoUri = takenPhotoUri!!)
        }

        composable(Destination.PlantPicturesScreen.route + "/{plantId}",
            arguments = listOf(
                navArgument("plantId"){
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )) {
            val plantId = it.arguments?.getLong("plantId")
            PlantPicturesScreen(navigation = navigation, plantId = plantId!!)
        }

        composable(Destination.MapScreen.route) {
            MapScreen(navigation)
        }

        composable(Destination.SettingsScreen.route) {
            SettingsScreen(navigation)
        }

        composable(Destination.CameraScreen.route) {
            CameraScreen(navigation)
        }
    }
}