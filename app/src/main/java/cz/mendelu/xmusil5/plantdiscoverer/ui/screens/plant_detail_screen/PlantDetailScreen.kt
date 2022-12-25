package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen

import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.*
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.LanguageUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import org.koin.androidx.compose.getViewModel

@Composable
fun PlantDetailScreen(
    navigation: INavigationRouter,
    plantId: Long,
    viewModel: PlantDetailViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = stringResource(id = R.string.plantDetail),
        navigation = navigation,
        showBackArrow = true,
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            IconButton(onClick = {
                // TODO
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        content = {
            PlantDetailScreenContent(navigation = navigation, viewModel = viewModel, plantId = plantId)
        }
    )
}

@Composable
fun PlantDetailScreenContent(
    navigation: INavigationRouter,
    viewModel: PlantDetailViewModel,
    plantId: Long
){
    viewModel.plantDetailUiState.value.let {
        when(it){
            is PlantDetailUiState.Start -> {
                LaunchedEffect(it){
                    if (plantId >= 0){
                        viewModel.loadPlant(plantId)
                    } else {
                        viewModel.plantDetailUiState.value = PlantDetailUiState.Error(R.string.plantNotFound)
                    }
                }
            }
            is PlantDetailUiState.PlantLoaded -> {
                PlantDetailForm(navigation = navigation, plant = it.plant)
            }
            is PlantDetailUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorCode))
            }
        }
    }
}

@Composable
fun PlantDetailForm(
    navigation: INavigationRouter,
    plant: Plant
){
    val locationString = remember{
        mutableStateOf("-")
    }
    val geocoder = Geocoder(LocalContext.current, java.util.Locale.getDefault())
    if (plant.latitude != null && plant.longitude != null){
        geocoder.getFromLocation(plant.latitude!!, plant.longitude!!, 1) {
            it.firstOrNull()?.let {
                locationString.value = "${it.subAdminArea}, ${it.countryCode}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Image(
                bitmap = PictureUtils.fromByteArrayToBitmap(plant.photo)?.asImageBitmap() ?: ImageBitmap.imageResource(
                    id = R.drawable.ic_error
                ),
                contentDescription = stringResource(id = R.string.plantImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
            )
        }


        Column(
            modifier = Modifier
                .padding(top = 10.dp)
        ) {
            CustomDetailRow(
                title = stringResource(id = R.string.name),
                text = plant.name, iconId = R.drawable.ic_plant)
            CustomDetailRowWithAdditionalLabel(
                title = stringResource(id = R.string.originalMatch),
                text = plant.originalMatch,
                additionalLabel = "${plant.originalCertainty}%",
                iconId = R.drawable.ic_machinelearning
            )
            CustomDetailRow(
                title = stringResource(id = R.string.placeDiscovered),
                text = locationString.value,
                iconId = R.drawable.ic_globe
            )
            CustomDetailRowWithAdditionalButton(title = stringResource(id = R.string.queryString),
                text = plant.imageQuery,
                iconId = R.drawable.ic_query,
                additionalButtonIconId = R.drawable.ic_query,
                onButtonClick = {
                    navigation.toPlantImagesScreen(plant.imageQuery)
                })
            CustomDetailRow(
                title = stringResource(id = R.string.dateDiscovered),
                text = DateUtils.getDateString(plant.dateDiscovered),
                iconId = R.drawable.ic_calendar)
            CustomDetailRow(
                title = stringResource(id = R.string.description),
                text = plant.description ?: "",
                iconId = R.drawable.ic_note)
        }
    }
}