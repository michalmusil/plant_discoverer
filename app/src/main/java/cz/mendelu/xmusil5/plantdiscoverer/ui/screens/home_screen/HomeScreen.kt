package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.code_models.Month
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.*
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ui_elements.MonthlyColumnChart
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ui_elements.StatisticsCard
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.plants_list_screen.PlantsListScreenContent
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
                    }
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