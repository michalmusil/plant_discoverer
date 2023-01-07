package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.BigImageWithText
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ui_elements.StatisticsCard
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import org.koin.androidx.compose.getViewModel
import java.util.Calendar

@Composable
fun HomeScreen(
    navigation: INavigationRouter,
    viewModel: HomeViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = "Home",
        navigation = navigation,
        content = {
            HomeScreenContent(navigation = navigation, viewModel = viewModel)
        }
    )
}

@Composable
fun HomeScreenContent(
    navigation: INavigationRouter,
    viewModel: HomeViewModel
){
    val totalNumberOfPlants = remember {
        mutableStateOf(0L)
    }

    val latestDiscoveredPlant = remember {
        mutableStateOf<Plant?>(null)
    }

    viewModel.homeUiState.value.let {
        when(it){
            is HomeUiState.Start -> {
                LoadingScreen()
                LaunchedEffect(it){
                    viewModel.fetchStatistics(
                        DateUtils.getCurrentDate().get(Calendar.YEAR)
                    )
                }
            }
            is HomeUiState.StatisticsLoaded -> {
                totalNumberOfPlants.value = it.totalNumOfPlants
                latestDiscoveredPlant.value = it.latestPlant
                HomeDashBoard(navigation, viewModel, totalNumberOfPlants.value, latestDiscoveredPlant.value)
            }
            is HomeUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorCode))
            }
        }
    }
}

@Composable
fun HomeDashBoard(
    navigation: INavigationRouter,
    viewModel: HomeViewModel,
    numberOfPlants: Long,
    latestPlant: Plant?
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        latestPlant?.photo?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp, top = 16.dp)
            ) {
                BigImageWithText(
                    photo = PictureUtils.fromByteArrayToBitmap(it)?.asImageBitmap()
                        ?: ImageBitmap.imageResource(id = R.drawable.ic_error),
                    aspectRatio = 1.4f,
                    contentDescription = stringResource(id = R.string.plantImage),
                    titleText = stringResource(id = R.string.latestDiscovery),
                    secondaryText = latestPlant.name,
                    onClick = {
                        navigation.toPlantDetailScreen(latestPlant.id!!)
                    }
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            StatisticsCard(
                label = stringResource(id = R.string.totalNumberOfPlants),
                value = numberOfPlants.toString(),
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f)
            )
            latestPlant?.let {
                StatisticsCard(
                    label = stringResource(id = R.string.lastDateDiscovered),
                    value = DateUtils.getDateString(it.dateDiscovered),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    onClick = { navigation.toPlantDetailScreen(it.id!!) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}