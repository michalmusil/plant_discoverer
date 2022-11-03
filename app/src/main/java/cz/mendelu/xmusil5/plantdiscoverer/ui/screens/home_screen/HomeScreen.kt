package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.navigation.Destination
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen.MapViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    navigation: INavigationRouter,
    viewModel: HomeViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = "Home",
        navigation = navigation,
        content = {
            Text(text = "Hello world", fontSize = 24.sp)
        }
    )
}
