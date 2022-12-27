package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.Manifest
import android.graphics.Bitmap
import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.objects.DetectedObject
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items.ImageLabelListItem
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.grayCommon
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import org.koin.androidx.compose.getViewModel

@Composable
fun NewPlantScreen(
    navigation: INavigationRouter,
    viewModel: NewPlantViewModel = getViewModel(),
    takenPhotoUri: String
){
    ScreenSkeleton(
        topBarText = stringResource(id = R.string.newPlant),
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NewPlantScreenContent(
    navigation: INavigationRouter,
    viewModel: NewPlantViewModel,
    takenPhotoUri: String
){
    val locationPermissionState = rememberMultiplePermissionsState(
        permissions =
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver{ _, event ->
            if (event == Lifecycle.Event.ON_START && !locationPermissionState.allPermissionsGranted) {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    val currentLocation = rememberSaveable {
        mutableStateOf<Location?>(null)
    }

    if (locationPermissionState.allPermissionsGranted && currentLocation.value == null){
        viewModel.getCurrentUserLocation(LocalContext.current) {
            currentLocation.value = it
        }
    }

    viewModel.newPlantUiState.value.let {
        when(it){
            is NewPlantUiState.Start -> {
                val context = LocalContext.current
                LaunchedEffect(it) {
                    viewModel.getImageFromUri(context, takenPhotoUri)
                }
                LoadingScreen()
            }
            is NewPlantUiState.PhotoLoaded -> {
                LaunchedEffect(it){
                    viewModel.reckognizePhoto(it.photo)
                }
            }
            is NewPlantUiState.ImageReckognized -> {
                NewPlantForm(navigation = navigation, viewModel = viewModel, photo = it.photo, detectedObject = it.detectedObject, location = currentLocation.value)
            }
            is NewPlantUiState.ImageReckognitionFailed -> {
                NewPlantForm(navigation = navigation, viewModel = viewModel, photo = it.photo, detectedObject = null, location = currentLocation.value)
            }
            is NewPlantUiState.NewPlantSaved -> {
                LaunchedEffect(it){
                    navigation.toPlantsListScreen()
                }
            }
            is NewPlantUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.code))
            }
        }
    }
}

@Composable
fun NewPlantForm(
    navigation: INavigationRouter,
    viewModel: NewPlantViewModel,
    photo: Bitmap,
    detectedObject: DetectedObject?,
    location: Location?
){
    val detectedObjectName = detectedObject?.labels?.firstOrNull()?.text ?: "-"
    val detectedObjectConficence = detectedObject?.labels?.firstOrNull()?.confidence ?: 0F

    val selectedDetectedObjectLabel = remember{
        mutableStateOf<DetectedObject.Label?>(detectedObject?.labels?.firstOrNull())
    }

    val name = rememberSaveable {
        mutableStateOf("")
    }
    val dateDiscoveded = rememberSaveable {
        mutableStateOf(DateUtils.getCurrentDate())
    }
    val imageQuery = rememberSaveable {
        mutableStateOf("")
    }
    val description = rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(detectedObject){
        if (detectedObject != null){
            name.value = detectedObjectName
            imageQuery.value = detectedObjectName
        }
    }

    // FORM VALIDATION
    val nameError = rememberSaveable{
        mutableStateOf(false)
    }
    val imageQueryError = rememberSaveable{
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // PHOTO
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(photo),
                contentDescription = stringResource(id = R.string.plantImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
        
        // IMAGE RECKOGNITION
        if (detectedObject != null && detectedObject.labels.isNotEmpty()){
            ImageReckognitionResults2(labels = detectedObject.labels, selectedLabel = selectedDetectedObjectLabel)
        }


        // FORM ITEMS
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Column(
               horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier
                   .fillMaxWidth()
           ) {
               //TEXT FIELDS
               CustomTextField(
                   labelTitle = stringResource(id = R.string.name),
                   value = name,
                   maxChars = 50,
                   isError = nameError.value,
                   errorMessage = stringResource(id = R.string.nameTooShort),
                   onTextChanged = {
                       nameError.value = it.isBlank()
                   }
               )
               CustomTextField(
                   labelTitle = stringResource(id = R.string.queryString),
                   value = imageQuery,
                   maxChars = 50,
                   isError = imageQueryError.value,
                   errorMessage = stringResource(id = R.string.imageQueryTooShort),
                   onTextChanged = {
                       imageQueryError.value = it.isBlank()
                   }
               )
                CustomTextField(
                    labelTitle = stringResource(id = R.string.description),
                    value = description,
                    singleLine = false,
                )
               

               // BUTTONS
               Row(
                   horizontalArrangement = Arrangement.SpaceBetween,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(vertical = 20.dp)
               ) {
                   CustomOutlinedButton(
                       text = stringResource(id = R.string.discard),
                       backgroundColor = grayCommon,
                       textColor = MaterialTheme.colorScheme.onSecondary,
                       modifier = Modifier
                           .weight(1f)
                           .padding(horizontal = 5.dp),
                       onClick = {
                           navigation.returnBack()
                       }
                   )
                   CustomOutlinedButton(
                       text = stringResource(id = R.string.save),
                       backgroundColor = MaterialTheme.colorScheme.secondary,
                       textColor = MaterialTheme.colorScheme.onSecondary,
                       modifier = Modifier
                           .weight(1f)
                           .padding(horizontal = 5.dp),
                       onClick = {
                           nameError.value = name.value.isBlank()
                           imageQueryError.value = imageQuery.value.isBlank()
                            if (
                                !nameError.value
                                && !imageQueryError.value
                            ){
                                val confidence = detectedObjectConficence * 100
                                val originalMatch = detectedObjectName
                                val newPlant = Plant(
                                    name = name.value,
                                    dateDiscovered = DateUtils.getCurrentUnixTime(),
                                    originalMatch = originalMatch,
                                    originalCertainty = confidence.toInt(),
                                    imageQuery = imageQuery.value,
                                )
                                newPlant.description = description.value
                                newPlant.photo = PictureUtils.fromBitmapToByteArray(photo)
                                newPlant.latitude = location?.latitude
                                newPlant.longitude = location?.longitude

                                viewModel.saveNewPlant(newPlant)
                            }
                       }
                   )
               }
           }
        }
    }
}


@Composable
fun ImageReckognitionResults(detectedObject: DetectedObject?){
    val borderColor = if (detectedObject != null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .border(2.dp, borderColor, RoundedCornerShape(15.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.Start, 
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_machinelearning),
                contentDescription = stringResource(id = R.string.machineLearning),
                Modifier
                    .padding(5.dp)
                    .padding(start = 15.dp)
                    .width(50.dp)
                    .aspectRatio(1f)
            )
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column (verticalArrangement = Arrangement.Center) {
                if (detectedObject != null) {
                    Text(
                        text = detectedObject.labels.firstOrNull()?.text ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    val percentage = String.format("%.0f", ((detectedObject.labels.firstOrNull()?.confidence?: 0F) * 100))
                    Text(
                        text = "${stringResource(id = R.string.confidence)}: ${percentage}%",
                        fontSize = 12.sp,
                        color = grayCommon
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.couldntReckognizePhoto),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ImageReckognitionResults2(
    labels: List<DetectedObject.Label>,
    selectedLabel: MutableState<DetectedObject.Label?>
){
    LazyRow(
        modifier = Modifier
            .padding(vertical = 4.dp)
    ) {
        items(labels){ item ->
            ImageLabelListItem(label = item, selectedLabel = selectedLabel) {
                if (selectedLabel.value == item){
                    selectedLabel.value = null
                } else {
                    selectedLabel.value = item
                }
            }
        }
    }
}