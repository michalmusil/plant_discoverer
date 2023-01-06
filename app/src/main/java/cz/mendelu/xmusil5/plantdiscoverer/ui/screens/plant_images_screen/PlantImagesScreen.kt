package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_images_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.communication.CommunicationResult
import cz.mendelu.xmusil5.plantdiscoverer.model.api_models.UnsplashImage
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items.PlantImageGridListItem
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow
import cz.mendelu.xmusil5.plantdiscoverer.utils.isConnectedToInternet
import cz.mendelu.xmusil5.plantdiscoverer.utils.onLastReached
import kotlinx.coroutines.launch
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
    val startingImagePage = 1

    viewModel.plantPicturesUiState.value.let {
        when (it) {
            is PlantImagesUiState.Start -> {
                if (isConnectedToInternet(LocalContext.current)) {
                    LaunchedEffect(it) {
                        viewModel.fetchImages(query, startingImagePage)
                    }
                    LoadingScreen()
                } else {
                    ErrorScreen(
                        text = stringResource(id = R.string.noInternet),
                        imageResourceId = R.drawable.ic_no_wifi
                    )
                }
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

    val showImageDetailPopup = remember {
        mutableStateOf(false)
    }

    val clickedImageUrl = remember{
        mutableStateOf<String?>(null)
    }

    LazyVerticalGrid(
        state = gridListState,
        columns = GridCells.Adaptive(145.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ){
        items(images){ image ->
            PlantImageGridListItem(imageAddress = image.imageUrls.regular, onClick = {
                if (!showImageDetailPopup.value) {
                    clickedImageUrl.value = image.imageUrls.regular
                    showImageDetailPopup.value = true
                }
            })
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

    ImageInDetailPopup(
        imageUrl = clickedImageUrl.value,
        showImagePopup = showImageDetailPopup,
        onDismiss = {
            showImageDetailPopup.value = false
            clickedImageUrl.value = null
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ImageInDetailPopup(
    imageUrl: String?,
    showImagePopup: MutableState<Boolean>,
    onDismiss: () -> Unit
){
    val context = LocalContext.current
    val cornerRadius = 12.dp

    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismiss
    ) {
        AnimatedVisibility(
            visible = showImagePopup.value,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        onDismiss()
                    }
            ){
                imageUrl?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.plantImage),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .customShadow(color = shadowColor,
                                borderRadius = cornerRadius,
                                spread = 5.dp,
                                blurRadius = 10.dp,
                                offsetY = 3.dp
                            )
                            .clip(RoundedCornerShape(cornerRadius))
                    )
                }
            }
        }
    }
}


