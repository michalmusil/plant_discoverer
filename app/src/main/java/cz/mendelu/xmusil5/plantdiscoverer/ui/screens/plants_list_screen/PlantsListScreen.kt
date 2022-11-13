package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.activities.MainActivity
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.CameraFloatingActionButton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
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
    val plants = mutableStateListOf<Plant>()

    viewModel.plantsListUiState.value.let { state ->
        when(state){
            is PlantsListUiState.Start -> {
                LoadingScreen()
                LaunchedEffect(state){
                    viewModel.loadPlants()
                }
            }
            is PlantsListUiState.DataLoaded -> {
                plants.addAll(state.plants)
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
    plants: List<Plant>,
){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        content = {
            items(plants) { plant ->
                PlantGridListItem(
                    plant = plant,
                    onItemClick = {
                        navigation.toPlantDetailScreen(plant.id!!)
                    })
            }
        })
}

@Composable
fun PlantGridListItem(
    plant: Plant,
    onItemClick: (Plant) -> Unit
){
    val photoBitmap = PictureUtils.fromByteArrayToBitmap(plant.photo)
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .padding(3.dp)
            .clickable {
                onItemClick(plant)
            }
    ){
        Image(
            bitmap = photoBitmap?.asImageBitmap() ?: ImageBitmap.imageResource(id = R.drawable.ic_error),
            contentDescription = "desc",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(5.dp)
                .clip(RoundedCornerShape(8.dp)))
        Text(
            text = plant.name,
            fontSize = 16.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(9.dp))
                .background(color = MaterialTheme.colorScheme.secondary, shape = RectangleShape)
                .padding(horizontal = 5.dp),
            overflow = TextOverflow.Ellipsis

        )
    }
}