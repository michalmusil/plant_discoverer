package cz.mendelu.xmusil5.plantdiscoverer.navigation

sealed class Destination(val route: String){
    object PlantsListScreen: Destination("plants_list")
    object HomeScreen: Destination("home")
    object NewPlantScreen: Destination("new_plant")
    object PlantEditScreen: Destination("plant_edit")
    object PlantDetailScreen: Destination("plant_detail")
    object PlantImagesScreen: Destination("plant_images")
    object MapScreen: Destination("map")
    object SettingsScreen: Destination("settings")
    object CameraScreen: Destination("camera_screen")
}
