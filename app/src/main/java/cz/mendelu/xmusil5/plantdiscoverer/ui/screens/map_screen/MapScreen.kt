package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.GridBasedAlgorithm
import com.google.maps.android.compose.*
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.map.MarkerUtils
import cz.mendelu.xmusil5.plantdiscoverer.map.PlantDiscovererMapRenderer
import cz.mendelu.xmusil5.plantdiscoverer.map.PlantMarker
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.CustomOutlinedButton
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.grayCommon
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun MapScreen(
    navigation: INavigationRouter,
    viewModel: MapViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = stringResource(id = R.string.map),
        navigation = navigation,
        content = {
            MapScreenContent(navigation = navigation, viewModel = viewModel)
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreenContent(
    navigation: INavigationRouter,
    viewModel: MapViewModel
){
    val context = LocalContext.current
    
    val plants = remember{
        mutableStateListOf<Plant>()
    }
    
    val currentLocation = rememberSaveable {
        mutableStateOf<Location?>(null)
    }

    val locationPermissionState = rememberMultiplePermissionsState(
        permissions =
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)

    )

    val lifecycleOwner = LocalLifecycleOwner.current

    // Observer for launching permission request
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

    //Observer for launching map view when permissions get accepted
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver{_, event ->
            if (viewModel.mapUiState.value is MapUiState.PermissionsDenied && event == Lifecycle.Event.ON_RESUME && locationPermissionState.allPermissionsGranted) {
                viewModel.mapUiState.value = MapUiState.Start()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
    
    // In case permissions get revoked, ui state is switched to denied permissions
    if (viewModel.mapUiState.value != MapUiState.PermissionsDenied() &&
        !locationPermissionState.allPermissionsGranted) {
        viewModel.mapUiState.value = MapUiState.PermissionsDenied()
    }

    viewModel.mapUiState.value.let {
        when (it) {
            is MapUiState.Start -> {
                if (!locationPermissionState.allPermissionsGranted){
                    viewModel.mapUiState.value = MapUiState.PermissionsDenied()
                } else {
                    LoadingScreen()
                    LaunchedEffect(it){
                        viewModel.getCurrentUserLocation(context)
                    }
                }
            }
            is MapUiState.LocationResults -> {
                LaunchedEffect(it){
                    currentLocation.value = it.location
                    viewModel.loadPlantsWithLocation()
                }
            }
            is MapUiState.PlantsLoaded -> {
                plants.clear()
                plants.addAll(it.plants)
                PlantsMap(plants = plants, currentLocation = currentLocation.value, navigation = navigation)
            }
            is MapUiState.PermissionsDenied -> {
                ErrorScreen(text = stringResource(id = R.string.locationForbidden), imageResourceId = R.drawable.ic_location_forbidden)
            }
            is MapUiState.Error -> {
                ErrorScreen(text = stringResource(id = it.errorCode))
            }
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun PlantsMap(
    plants: List<Plant>,
    currentLocation: Location?,
    navigation: INavigationRouter
){
    val defaultZoom = 9f
    val coroutineScope = LocalLifecycleOwner.current.lifecycleScope

    val mapUiSettings by remember { mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = false,
            mapToolbarEnabled = false)
    ) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
            currentLocation?.latitude ?: 0.0,
                currentLocation?.longitude ?:  0.0), defaultZoom)
    }

    val showPopup = rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    var clusterManager by remember {
        mutableStateOf<ClusterManager<Plant>?>(null)
    }
    var clusterRenderer by remember {
        mutableStateOf<PlantDiscovererMapRenderer?>(null)
    }
    var lastClickedMarker = remember {
        mutableStateOf<PlantMarker?>(null)
    }

    if (!plants.isEmpty()){
        clusterManager?.addItems(plants)
        clusterManager?.cluster()
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(modifier = Modifier.fillMaxHeight(),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState
        ){

            MapEffect(plants) { map ->
                if (clusterManager == null){
                    clusterManager = ClusterManager<Plant>(context, map)
                }
                if (clusterRenderer == null){
                    clusterRenderer = PlantDiscovererMapRenderer(context, map, clusterManager!!)
                }

                clusterManager?.apply {
                    renderer = clusterRenderer

                    renderer.setOnClusterItemClickListener { item ->
                        // Move camera to position
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                update = CameraUpdateFactory.newCameraPosition(CameraPosition(item.position, defaultZoom, 0f, 0f)),
                                durationMs = 500
                            )
                        }
                        // If there was a marker user clicked before, its not highlighted anymore
                        unHighlightMarker(context, lastClickedMarker)

                        // Now the clicked marker state is saved
                        lastClickedMarker.value = PlantMarker(item, clusterRenderer?.getMarker(item)!!)
                        lastClickedMarker.value?.marker?.setIcon(
                            BitmapDescriptorFactory.fromBitmap(
                                MarkerUtils.createCustomMarkerFromLayout(context, item!!, true)
                            )
                        )
                        showPopup.value = true
                        true
                    }
                }

                map.setOnCameraMoveListener {
                    clusterManager?.cluster()
                }

                map.setOnCameraIdleListener {
                    clusterManager?.cluster()
                }
            }
        }

        PlantMapPopup(plant = lastClickedMarker.value?.plant, navigation = navigation, showPopup){
            showPopup.value = false
            unHighlightMarker(context, lastClickedMarker)
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlantMapPopup(
    plant: Plant?,
    navigation: INavigationRouter,
    showPopupState: MutableState<Boolean>,
    onDismiss: () -> Unit
){
    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { onDismiss() }
    ) {
        AnimatedVisibility(
            visible = showPopupState.value,
            enter = scaleIn(),
            exit = scaleOut()

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                plant?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                bitmap = PictureUtils.fromByteArrayToBitmap(plant.photo)
                                    ?.asImageBitmap()
                                    ?: ImageBitmap.imageResource(id = R.drawable.ic_error),
                                contentDescription = plant.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(65.dp)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clip(CircleShape)
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp, top = 5.dp)
                            ) {
                                Text(
                                    text = plant.name,
                                    fontSize = 20.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                plant.description?.let {
                                    Text(
                                        text = it,
                                        fontSize = 12.sp,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 17.sp
                                    )
                                }
                            }

                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomOutlinedButton(
                                text = stringResource(id = R.string.editPlant),
                                backgroundColor = grayCommon,
                                textColor = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 5.dp),
                                textSize = 14.sp,
                                onClick = {
                                    navigation.toPlantEditScreen(plant.id!!)
                                    onDismiss()
                                }
                            )
                            CustomOutlinedButton(
                                text = stringResource(id = R.string.plantDetail),
                                backgroundColor = MaterialTheme.colorScheme.secondary,
                                textColor = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 5.dp),
                                textSize = 14.sp,
                                onClick = {
                                    navigation.toPlantDetailScreen(plant.id!!)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun unHighlightMarker(
    context: Context,
    lastClickedMarker: MutableState<PlantMarker?>
){
    lastClickedMarker.value?.let {
        it.marker.setIcon(
            BitmapDescriptorFactory.fromBitmap(
                MarkerUtils.createCustomMarkerFromLayout(context, it.plant, false)
            )
        )
        lastClickedMarker.value = null
    }
}

