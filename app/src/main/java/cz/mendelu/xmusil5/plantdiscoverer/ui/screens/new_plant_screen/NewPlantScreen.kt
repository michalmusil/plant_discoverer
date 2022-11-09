package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import org.koin.androidx.compose.getViewModel

@Composable
fun NewPlantScreen(
    navigation: INavigationRouter,
    viewModel: NewPlantViewModel = getViewModel(),
    takenPhotoUri: String
){
    ScreenSkeleton(
        topBarText = "New plant",
        navigation = navigation,
        showBackArrow = true,
        onBackClick = {
            navigation.returnBack()
        },
        content = {
            NewPlantScreenContent(navigation = navigation, viewModel = viewModel, takenPhotoUri = takenPhotoUri)
        }
    )
}

@Composable
fun NewPlantScreenContent(
    navigation: INavigationRouter,
    viewModel: NewPlantViewModel,
    takenPhotoUri: String
){
    viewModel.newPlantUiState.value.let {
        when(it){
            is NewPlantUiState.Start -> {
                val context = LocalContext.current
                LaunchedEffect(it) {
                    viewModel.getImageFromUri(context, takenPhotoUri)
                }
            }
            is NewPlantUiState.PhotoLoaded -> {
                NewPlantForm(photo = it.photo)
            }
            is NewPlantUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.code))
            }
        }
    }
}

@Composable
fun NewPlantForm(
    photo: Bitmap
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                bitmap = photo.asImageBitmap(),
                contentDescription = stringResource(id = R.string.plantImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}