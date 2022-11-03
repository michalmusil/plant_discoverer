package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import org.koin.androidx.compose.getViewModel

@Composable
fun MapScreen(
    navigation: INavigationRouter,
    viewModel: MapViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = "Map",
        navigation = navigation,
        content = {
            Text(text = "Hello world", fontSize = 24.sp)
        }
    )
}
