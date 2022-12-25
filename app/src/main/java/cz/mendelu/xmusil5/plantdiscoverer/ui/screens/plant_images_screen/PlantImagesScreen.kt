package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items.PlantImageGridListItem
import org.koin.androidx.compose.getViewModel

@Composable
fun PlantImagesScreen(
    navigation: INavigationRouter,
    query: String,
    viewModel: PlantImagesViewModel = getViewModel()
){
    ScreenSkeleton(
        topBarText = stringResource(id = R.string.plantImages),
        navigation = navigation,
        showBackArrow = true,
        onBackClick = {
            navigation.returnBack()
        },
        content = {
            PlantImagesScreenContent(query = query, viewModel = viewModel, navigation = navigation)
        }
    )
}

@Composable
fun PlantImagesScreenContent(
    query: String,
    viewModel: PlantImagesViewModel,
    navigation: INavigationRouter
){
    viewModel.plantPicturesUiState.value.let {
        when (it) {
            is PlantImagesUiState.Start -> {
                LaunchedEffect(it) {
                    viewModel.fetchImages(query)
                }
                LoadingScreen()
            }
            is PlantImagesUiState.ImagesLoaded -> {
                PlantImagesList(imagesList = it.images)
            }
            is PlantImagesUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorCode))
            }
        }
    }
}

@Composable
fun PlantImagesList(imagesList: List<UnsplashImage>){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ){
        items(imagesList){ image ->
            PlantImageGridListItem(imageAddress = image.imageUrls.regular)
        }
    }
}


