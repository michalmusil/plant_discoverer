package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen

import androidx.compose.runtime.Composable
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import org.koin.androidx.compose.getViewModel


@Composable
fun PlantEditScreen(
    navigation: INavigationRouter,
    plantId: Long,
    viewModel: PlantEditViewModel = getViewModel()
){

}