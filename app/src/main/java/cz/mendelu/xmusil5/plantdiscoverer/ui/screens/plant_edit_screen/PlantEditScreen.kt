package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import org.koin.androidx.compose.getViewModel


@Composable
fun PlantEditScreen(
    navigation: INavigationRouter,
    plantId: Long,
    viewModel: PlantEditViewModel = getViewModel()
){
    ScreenSkeleton(
        topBarText = "Edit plant",
        navigation = navigation,
        showBackArrow = true,
        onBackClick = {
            print("Clicked back")
        },
        content = {
            Text(text = "Hello world", fontSize = 24.sp)
        }
    )
}