package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.code_models.Month
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.screens.NoDataScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ui_elements.MonthlyColumnChart
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ui_elements.StatisticsCard
import cz.mendelu.xmusil5.plantdiscoverer.utils.DateUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import org.koin.androidx.compose.getViewModel
import java.util.Calendar

const val TAG_NAVIGATION_SETTINGS = "navigationSettings"
const val TAG_TOTAL_NUMBER_OF_PLANTS = "totalNumberOfDiscoveredPlants"
const val TAG_LAST_DISCOVERY_DATE = "lastDiscoveryDate"
const val TAG_LAST_DISCOVERY_CARD = "lastDiscoveryCard"

@Composable
fun HomeScreen(
    navigation: INavigationRouter,
    viewModel: HomeViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = stringResource(id = R.string.home),
        navigation = navigation,
        actions = {
            IconButton(
                onClick = {
                    navigation.toSettingsScreen()
                },
                modifier = Modifier.testTag(TAG_NAVIGATION_SETTINGS)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                    contentDescription = stringResource(id = R.string.settings),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                HomeScreenContent(navigation = navigation, viewModel = viewModel)
                CameraFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onSuccessfullCameraClick = {
                        navigation.toCameraScreen()
                    }
                )
            }
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

    val monthlyValues = remember {
        mutableStateOf<HashMap<Month, Double>>(hashMapOf())
    }

    val activeYears = remember{
        mutableStateListOf<Int>()
    }

    viewModel.homeUiState.value.let {
        when(it){
            is HomeUiState.Start -> {
                LoadingScreen()
                LaunchedEffect(it){
                    //viewModel.addTestData()
                    viewModel.fetchStatistics(
                        DateUtils.getCurrentDate().get(Calendar.YEAR)
                    )
                }
            }
            is HomeUiState.StatisticsLoaded -> {
                totalNumberOfPlants.value = it.totalNumOfPlants
                latestDiscoveredPlant.value = it.latestPlant
                monthlyValues.value = it.monthCounts
                activeYears.apply {
                    clear()
                    addAll(it.acitveYears)
                }

                HomeDashBoard(
                    navigation = navigation,
                    viewModel = viewModel,
                    numberOfPlants = totalNumberOfPlants.value,
                    latestPlant = latestDiscoveredPlant.value,
                    monthlyValues = monthlyValues.value,
                    activeYears = activeYears
                )
            }
            is HomeUiState.NoData -> {
                NoDataScreen()
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
    latestPlant: Plant?,
    monthlyValues: HashMap<Month, Double>,
    activeYears: List<Int>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        // Latest discovery photo
        latestPlant?.photo?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                BigImageWithText(
                    photo = PictureUtils.fromByteArrayToBitmap(it)?.asImageBitmap()
                        ?: PictureUtils.getBitmapFromVectorDrawable(
                            LocalContext.current, R.drawable.ic_error)!!.asImageBitmap(),
                    aspectRatio = 1.4f,
                    contentDescription = stringResource(id = R.string.plantImage),
                    titleText = stringResource(id = R.string.latestDiscovery),
                    secondaryText = latestPlant.name,
                    onClick = {
                        navigation.toPlantDetailScreen(latestPlant.id!!)
                    },
                    modifier = Modifier.testTag(TAG_LAST_DISCOVERY_CARD)
                )
            }
        }

        // Statistics cards
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(100.dp)
        ) {
            StatisticsCard(
                label = stringResource(id = R.string.totalNumberOfPlants),
                value = numberOfPlants.toString(),
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                modifierCard = Modifier.weight(1f),
                modifierValue = Modifier.testTag(TAG_TOTAL_NUMBER_OF_PLANTS)
            )
            latestPlant?.let {
                StatisticsCard(
                    label = stringResource(id = R.string.lastDateDiscovered),
                    value = DateUtils.getDateString(it.dateDiscovered),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    onClick = { navigation.toPlantDetailScreen(it.id!!) },
                    modifierCard = Modifier.weight(1f),
                    modifierValue = Modifier.testTag(TAG_LAST_DISCOVERY_DATE)
                )
            }
        }


        // Monthly discoveries chart with filter
        val yearFilterList = activeYears.map {
            it.toString()
        }
        val selectedFilterItem = remember {
            mutableStateOf(yearFilterList.firstOrNull())
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 26.dp)
        ) {

            MonthlyColumnChart(
                title = stringResource(id = R.string.monthlyDiscoveries),
                data = monthlyValues,
                columnColor = MaterialTheme.colorScheme.primary,
                scaleColor = MaterialTheme.colorScheme.onBackground,
                filterItems = yearFilterList,
                selectedFilterItem = selectedFilterItem,
                onFilterItemClick = {
                    if (it == selectedFilterItem.value){
                        selectedFilterItem.value = null
                    } else {
                        selectedFilterItem.value = it
                    }
                    it.toIntOrNull()?.let {
                        viewModel.fetchStatistics(it)
                    }
                }
            )

        }
    }
}