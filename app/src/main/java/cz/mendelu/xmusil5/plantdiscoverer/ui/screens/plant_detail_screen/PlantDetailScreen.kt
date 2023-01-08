package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_detail_screen

import android.graphics.drawable.VectorDrawable
import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.*
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
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
                viewModel.deletePlant(plantId){
                    navigation.returnBack()
                }
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_trash),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = {
                navigation.toPlantEditScreen(plantId = plantId)
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

    val context = LocalContext.current
    LaunchedEffect(plant){
        val geocoder = Geocoder(context, java.util.Locale.getDefault())
        if (plant.latitude != null && plant.longitude != null){
            geocoder.getFromLocation(plant.latitude!!, plant.longitude!!, 1) {
                it.firstOrNull()?.let {
                    locationString.value = "${it.subAdminArea}, ${it.countryCode}"
                }
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
            BigImage(
                photo = (PictureUtils.fromByteArrayToBitmap(plant.photo)?.asImageBitmap()
                    ?: PictureUtils.getBitmapFromVectorDrawable(
                        LocalContext.current, R.drawable.ic_error)!!.asImageBitmap()),
                contentDescription = stringResource(id = R.string.plantImage)
            )
        }


        Column(
            modifier = Modifier
                .padding(top = 10.dp)
        ) {
            CustomDetailRow(
                title = stringResource(id = R.string.name),
                text = plant.name, iconId = R.drawable.ic_plant)

            if (plant.originalCertainty > 0) {
                CustomDetailRowWithAdditionalLabel(
                    title = stringResource(id = R.string.originalMatch),
                    text = plant.originalMatch,
                    additionalLabel = "${plant.originalCertainty}%",
                    iconId = R.drawable.ic_machinelearning
                )
            }
            CustomDetailRow(
                title = stringResource(id = R.string.placeDiscovered),
                text = locationString.value,
                iconId = R.drawable.ic_globe
            )
            CustomDetailRowWithAdditionalButton(title = stringResource(id = R.string.queryString),
                text = plant.imageQuery,
                iconId = R.drawable.ic_query,
                buttonText = stringResource(id = R.string.search),
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
                iconId = R.drawable.ic_note,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
        }
    }
}