package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.grayCommon
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.viewModel
import java.util.Calendar

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
                LoadingScreen()
            }
            is NewPlantUiState.PhotoLoaded -> {
                NewPlantForm(photo = it.photo, navigation = navigation, viewModel = viewModel)
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
    photo: Bitmap
){
    val context = LocalContext.current

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
        mutableStateOf(context.getString(R.string.nameTooShort))
    }
    val imageQueryError = rememberSaveable{
        mutableStateOf(context.getString(R.string.imageQueryTooShort))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // PHOTO
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
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


        // FORM ITEMS
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Column(
               horizontalAlignment = Alignment.CenterHorizontally,
               modifier = Modifier.fillMaxWidth()
           ) {
               //TEXT FIELDS
               CustomTextField(
                   labelTitle = stringResource(id = R.string.name),
                   value = name,
                   errorMessage = nameError,
                   onTextChanged = {
                        if (it.isEmpty()){
                            nameError.value = context.getString(R.string.nameTooShort)
                        } else{
                            nameError.value = ""
                        }
                   }
               )
               CustomTextField(
                   labelTitle = stringResource(id = R.string.queryString),
                   value = imageQuery,
                   errorMessage = imageQueryError,
                   onTextChanged = {
                       if (it.isEmpty()){
                           imageQueryError.value = context.getString(R.string.imageQueryTooShort)
                       } else{
                           imageQueryError.value = ""
                       }
                   }
               )
                CustomTextField(
                    labelTitle = stringResource(id = R.string.description),
                    value = description,
                    singleLine = false
                )
               

               // BUTTONS
               Row(
                   horizontalArrangement = Arrangement.SpaceEvenly,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(vertical = 20.dp)
               ) {
                   CustomOutlinedButton(
                       text = stringResource(id = R.string.discard),
                       backgroundColor = grayCommon,
                       textColor = MaterialTheme.colorScheme.onSecondary,
                       onClick = {
                           navigation.returnBack()
                       }
                   )
                   CustomOutlinedButton(
                       text = stringResource(id = R.string.save),
                       backgroundColor = MaterialTheme.colorScheme.secondary,
                       textColor = MaterialTheme.colorScheme.onSecondary,
                       onClick = {
                            if (
                                nameError.value.isEmpty() &&
                                imageQueryError.value.isEmpty()
                            ){
                                val newPlant = Plant(
                                    name = name.value,
                                    dateDiscovered = DateUtils.getCurrentUnixTime(),
                                    originalMatch = name.value,
                                    originalCertainty = 60,
                                    imageQuery = imageQuery.value,
                                )
                                newPlant.describtion = description.value
                                newPlant.photo = PictureUtils.fromBitmapToByteArray(photo)

                                viewModel.saveNewPlant(newPlant)
                            } else{
                                // TODO - ELSE DISPLAY SOME KIND OF ERROR
                            }
                       }
                   )
               }
           }
        }
    }
}