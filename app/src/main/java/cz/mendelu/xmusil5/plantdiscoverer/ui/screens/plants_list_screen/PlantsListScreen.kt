package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.CameraFloatingActionButton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items.PlantGridListItemLarge
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.screens.NoDataScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun PlantsListScreen(
    navigation: INavigationRouter,
    viewModel: PlantsListViewModel = getViewModel()
){
    ScreenSkeleton(
        topBarText = stringResource(id = R.string.plantsList),
        navigation = navigation,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                PlantsListScreenContent(navigation = navigation, viewModel = viewModel)
                CameraFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onSuccessfullCameraClick = {
                        navigation.toCameraScreen()
                    }
                )
            }
        }    
    )
}

@Composable
fun PlantsListScreenContent(
    navigation: INavigationRouter,
    viewModel: PlantsListViewModel
){
    val plants = remember{
        mutableStateListOf<Plant>()
    }

    viewModel.plantsListUiState.value.let { state ->
        when(state){
            is PlantsListUiState.Start -> {
                LoadingScreen()
                LaunchedEffect(state){
                    viewModel.loadPlants()
                }
            }
            is PlantsListUiState.NoData -> {
                NoDataScreen()
            }
            is PlantsListUiState.DataLoaded -> {
                LaunchedEffect(state){
                   plants.clear()
                   plants.addAll(state.plants)
                }
                PlantsGridList(navigation = navigation, plants = plants)
            }
            is PlantsListUiState.Error -> {
                ErrorScreen(text = stringResource(id = state.errorId))
            }
        }
    }
}

@Composable
fun PlantsGridList(
    navigation: INavigationRouter,
    plants: SnapshotStateList<Plant>,
){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(
            count = plants.size,
            key = {
                plants[it].id!!
            },
            itemContent = { index ->
                PlantGridListItemLarge(
                    plant = plants[index],
                    onItemClick = {
                        navigation.toPlantDetailScreen(plants[index].id!!)
                    })
            }
        )
    }
}

