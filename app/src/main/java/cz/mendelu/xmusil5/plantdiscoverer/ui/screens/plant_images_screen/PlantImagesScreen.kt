package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items.PlantImageGridListItem
import cz.mendelu.xmusil5.plantdiscoverer.utils.onLastReached
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.viewModel

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
    val startingImagePage = 1

    viewModel.plantPicturesUiState.value.let {
        when (it) {
            is PlantImagesUiState.Start -> {
                LaunchedEffect(it) {
                    viewModel.fetchImages(query, startingImagePage)
                }
                LoadingScreen()
            }
            is PlantImagesUiState.ImagesLoaded -> {
                PlantImagesList(imagesList = it.images, query, startingImagePage, viewModel)
            }
            is PlantImagesUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorCode))
            }
        }
    }
}

@Composable
fun PlantImagesList(
    imagesList: List<UnsplashImage>,
    query: String,
    startingImagePage: Int,
    viewModel: PlantImagesViewModel
){

    val images = remember {
        mutableStateListOf<UnsplashImage>()
    }

    LaunchedEffect(imagesList){
        images.addAll(imagesList)
    }

    val imagePage = remember{
        mutableStateOf(startingImagePage)
    }
    val gridListState = rememberLazyGridState()

    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        state = gridListState,
        columns = GridCells.Adaptive(145.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ){
        items(images){ image ->
            PlantImageGridListItem(imageAddress = image.imageUrls.regular)
        }
    }

    gridListState.onLastReached {
        coroutineScope.launch {
            val newImages = viewModel.fetchAdditionalImages(query, imagePage.value + 1)
            if (newImages is CommunicationResult.Success) {
                imagePage.value += 1
                images.addAll(newImages.data.results)
            }
        }
    }
}


