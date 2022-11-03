package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import org.koin.androidx.compose.getViewModel

@Composable
fun NewPlantScreen(
    navigation: INavigationRouter,
    viewModel: NewPlantViewModel = getViewModel()
){
    ScreenSkeleton(
        topBarText = "New plant",
        navigation = navigation,
        showBackArrow = true,
        onBackClick = {
            print("Back")
        },
        content = {
            Text(text = "Hello world", fontSize = 24.sp)
        }
    )
}