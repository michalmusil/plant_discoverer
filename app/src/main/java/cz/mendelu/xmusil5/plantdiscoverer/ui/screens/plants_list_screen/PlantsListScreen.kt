package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import org.koin.androidx.compose.getViewModel

@Composable
fun PlantsListScreen(
    navigation: INavigationRouter,
    viewModel: PlantsListViewModel = getViewModel()
){
    ScreenSkeleton(
        topBarText = "Discovered plants",
        navigation = navigation,
        content = {
            Text(text = "Hello world", fontSize = 24.sp)
        }    
    )
}