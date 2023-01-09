package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.Manifest
import android.graphics.Bitmap
import android.location.Location
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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

const val TAG_NEW_PLANT_IMAGE = "newPlantImage"
const val TAG_NEW_PLANT_NAME_TEXT_FIELD = "newPlantNameTextField"
const val TAG_NEW_PLANT_QUERY_TEXT_FIELD = "newPlantQueryTextField"
const val TAG_NEW_PLANT_DESCRIPTION_TEXT_FIELD = "newPlantDescriptionTextField"
const val TAG_NEW_PLANT_SAVE_BUTTON = "newPlantSaveButton"
const val TAG_NEW_PLANT_DISCARD_BUTTON = "newPlantDiscardButton"

const val TAG_NEW_PLANT_EMPTY_SPOT = "newPlantEmptySpot"

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
                NewPlantForm(navigation = navigation, viewModel = viewModel, photo = it.photo, detectedObject = it.detectedObject, location = currentLocation)
            }
            is NewPlantUiState.ImageReckognitionFailed -> {
                NewPlantForm(navigation = navigation, viewModel = viewModel, photo = it.photo, detectedObject = null, location = currentLocation)
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
    location: MutableState<Location?>
){
    val localFocusManager = LocalFocusManager.current
    val selectedDetectedObjectLabel = remember{
        mutableStateOf<DetectedObject.Label?>(null)
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

    // FORM VALIDATION
    val nameError = rememberSaveable{
        mutableStateOf(false)
    }
    val imageQueryError = rememberSaveable{
        mutableStateOf(false)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        localFocusManager.clearFocus()
                    }
                )
            }
    ) {

        // PHOTO
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            BigImage(photo = photo.asImageBitmap(),
                contentDescription = stringResource(id = R.string.plantImage),
                modifier = Modifier.testTag(TAG_NEW_PLANT_IMAGE)
            )
        }

        Divider(
            modifier = Modifier
                .padding(vertical = 15.dp)
        )

        // IMAGE RECKOGNITION
        if (detectedObject != null && detectedObject.labels.isNotEmpty()){
            ImageReckognitionResults(
                labels = detectedObject.labels,
                selectedLabel = selectedDetectedObjectLabel,
                onNewLabelSelected = {
                    name.value = it.text
                    imageQuery.value = it.text
                }
            )
        } else {
            NoMLMatches()
        }

        Divider(
            modifier = Modifier
                .padding(vertical = 15.dp)
        )

        //TEXT FIELDS
        CustomTextField(
           labelTitle = stringResource(id = R.string.name),
           value = name,
           maxChars = 50,
           isError = nameError.value,
           errorMessage = stringResource(id = R.string.nameTooShort),
           onTextChanged = {
               nameError.value = it.isBlank()
           },
           modifierTextField = Modifier.testTag(TAG_NEW_PLANT_NAME_TEXT_FIELD)
        )
        CustomTextField(
           labelTitle = stringResource(id = R.string.queryString),
           value = imageQuery,
           maxChars = 50,
           isError = imageQueryError.value,
           errorMessage = stringResource(id = R.string.imageQueryTooShort),
           onTextChanged = {
               imageQueryError.value = it.isBlank()
           },
           modifierTextField = Modifier.testTag(TAG_NEW_PLANT_QUERY_TEXT_FIELD)
        )
        CustomTextField(
            labelTitle = stringResource(id = R.string.description),
            value = description,
            singleLine = false,
            modifierTextField = Modifier.testTag(TAG_NEW_PLANT_DESCRIPTION_TEXT_FIELD)
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
                   .padding(horizontal = 5.dp)
                   .testTag(TAG_NEW_PLANT_DISCARD_BUTTON),
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
                   .padding(horizontal = 5.dp)
                   .testTag(TAG_NEW_PLANT_SAVE_BUTTON),
               onClick = {
                   nameError.value = name.value.isBlank()
                   imageQueryError.value = imageQuery.value.isBlank()
                    if (
                        !nameError.value
                        && !imageQueryError.value
                    ){
                        val confidence = (selectedDetectedObjectLabel.value?.confidence ?: 0f)*100
                        val originalMatch = selectedDetectedObjectLabel.value?.text ?: "-"
                        val newPlant = Plant(
                            name = name.value,
                            dateDiscovered = DateUtils.getCurrentUnixTime(),
                            originalMatch = originalMatch,
                            originalCertainty = confidence.toInt(),
                            imageQuery = imageQuery.value,
                        )
                        newPlant.description = description.value
                        newPlant.photo = PictureUtils.fromBitmapToByteArray(photo)
                        newPlant.latitude = location.value?.latitude
                        newPlant.longitude = location.value?.longitude

                        viewModel.saveNewPlant(newPlant){
                            navigation.toPlantDetailScreen(it)
                        }
                    }
               }
           )
        }
    }
}


@Composable
fun NoMLMatches(){
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_machinelearning),
            contentDescription = stringResource(id = R.string.machineLearning),
            Modifier
                .padding(5.dp)
                .padding(start = 15.dp)
                .height(50.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(5.dp)
                .testTag(TAG_NEW_PLANT_EMPTY_SPOT)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Column (verticalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(id = R.string.couldntReckognizePhoto),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun ImageReckognitionResults(
    labels: List<DetectedObject.Label>,
    selectedLabel: MutableState<DetectedObject.Label?>,
    onNewLabelSelected: (label: DetectedObject.Label) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_machinelearning),
                contentDescription = stringResource(id = R.string.machineLearning),
                Modifier
                    .height(50.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(5.dp)
                    .testTag(TAG_NEW_PLANT_EMPTY_SPOT)
            )
            Text(
                text = stringResource(id = R.string.suggestions),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyRow(
            modifier = Modifier
                .padding(start = 5.dp)
        ){
            items(labels){ item ->
                ImageLabelListItem(label = item, selectedLabel = selectedLabel) {
                    if (selectedLabel.value == item){
                        selectedLabel.value = null
                    } else {
                        selectedLabel.value = item
                        onNewLabelSelected(item)
                    }
                }
            }
        }
    }

}