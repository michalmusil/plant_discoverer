package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plant_edit_screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.templates.DeleteDialog
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.grayCommon
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import org.koin.androidx.compose.getViewModel

const val TAG_PLANT_EDIT_PLANT_IMAGE = "plantEditPlantImage"
const val TAG_PLANT_EDIT_NAME_TEXT_FIELD = "plantEditNameTextField"
const val TAG_PLANT_EDIT_QUERY_TEXT_FIELD = "plantEditQueryTextField"
const val TAG_PLANT_EDIT_DESCRIPTION_TEXT_FIELD = "plantEditDescriptionTextField"

const val TAG_PLANT_EDIT_SAVE_BUTTON = "plantEditSaveButton"
const val TAG_PLANT_EDIT_CANCEL_BUTTON = "plantEditCancelButton"

const val TAG_PLANT_EDIT_EMPTY_SPOT = "plantEditEmptySpot"

@Composable
fun PlantEditScreen(
    navigation: INavigationRouter,
    plantId: Long,
    viewModel: PlantEditViewModel = getViewModel()
){
    val showDeleteDialog = rememberSaveable{
        mutableStateOf(false)
    }

    ScreenSkeleton(
        topBarText = stringResource(id = R.string.editPlant),
        navigation = navigation,
        showBackArrow = true,
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            IconButton(onClick = {
                showDeleteDialog.value = true
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_trash),
                    contentDescription = stringResource(id = R.string.delete),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        content = {
            if (showDeleteDialog.value) {
                DeleteDialog(
                    showDialog = showDeleteDialog, 
                    title = stringResource(id = R.string.areYouSureToDelete),
                    text = stringResource(id = R.string.actionIrreversible),
                    onConfirm = {
                        viewModel.deletePlant(plantId){
                            navigation.toPlantsListScreen()
                        }
                    }
                )
            }
            PlantEditScreenContent(
                plantId = plantId,
                navigation = navigation,
                viewModel = viewModel
            )
        }
    )
}

@Composable
fun PlantEditScreenContent(
    plantId: Long,
    navigation: INavigationRouter,
    viewModel: PlantEditViewModel
){
    viewModel.plantEditUiState.value.let {
        when (it) {
            is PlantEditUiState.Start -> {
                LaunchedEffect(it){
                    viewModel.loadPlant(plantId)
                }
                LoadingScreen()
            }
            is PlantEditUiState.PlantLoaded -> {
                PlantEditForm(plant = it.plant, viewModel = viewModel, navigation = navigation)
            }
            is PlantEditUiState.Error -> {
                ErrorScreen(
                    text = stringResource(id = it.errorCode)
                )
            }
        }
    }
}

@Composable
fun PlantEditForm(
    plant: Plant,
    viewModel: PlantEditViewModel,
    navigation: INavigationRouter
){
    val localFocusManager = LocalFocusManager.current
    val plantPhoto = PictureUtils.fromByteArrayToBitmap(plant.photo)

    val name = rememberSaveable {
        mutableStateOf(plant.name)
    }
    val imageQuery = rememberSaveable {
        mutableStateOf(plant.imageQuery)
    }
    val description = rememberSaveable {
        mutableStateOf(plant.description ?: "")
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        localFocusManager.clearFocus()
                    }
                )
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            BigImage(
                photo = plantPhoto?.asImageBitmap() ?: PictureUtils.getBitmapFromVectorDrawable(
                    LocalContext.current, R.drawable.ic_error)!!.asImageBitmap(),
                contentDescription = stringResource(id = R.string.plantImage),
                modifier = Modifier.testTag(TAG_PLANT_EDIT_PLANT_IMAGE)
            )
        }

        Divider(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .testTag(TAG_PLANT_EDIT_EMPTY_SPOT)
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
            modifierTextField = Modifier.testTag(TAG_PLANT_EDIT_NAME_TEXT_FIELD)
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
            modifierTextField = Modifier.testTag(TAG_PLANT_EDIT_QUERY_TEXT_FIELD)
        )
        CustomTextField(
            labelTitle = stringResource(id = R.string.description),
            value = description,
            singleLine = false,
            modifierTextField = Modifier.testTag(TAG_PLANT_EDIT_DESCRIPTION_TEXT_FIELD)
        )


        // BUTTONS
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            CustomOutlinedButton(
                text = stringResource(id = R.string.cancel),
                backgroundColor = grayCommon,
                textColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp)
                    .testTag(TAG_PLANT_EDIT_CANCEL_BUTTON),
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
                    .testTag(TAG_PLANT_EDIT_SAVE_BUTTON),
                onClick = {
                    nameError.value = name.value.isBlank()
                    imageQueryError.value = imageQuery.value.isBlank()
                    if (
                        !nameError.value &&
                        !imageQueryError.value
                    ){
                        plant.name = name.value
                        plant.imageQuery = imageQuery.value
                        plant.description = description.value
                        viewModel.saveChangesToPlant(plant){
                            navigation.returnBack()
                        }
                    }
                }
            )
        }
    }
}