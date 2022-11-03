package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen

import androidx.compose.runtime.Composable
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import org.koin.androidx.compose.getViewModel

@Composable
fun PlantDetailScreen(
    navigation: INavigationRouter,
    plantId: Long,
    viewModel: PlantDetailViewModel = getViewModel()
) {

}