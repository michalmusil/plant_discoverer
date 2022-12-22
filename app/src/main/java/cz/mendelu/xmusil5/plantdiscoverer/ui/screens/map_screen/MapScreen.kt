package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.map_screen

import android.Manifest
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ErrorScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
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
                PlantsMap(plants = plants, currentLocation = currentLocation.value)
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
    currentLocation: Location?
){

    val mapUiSettings by remember { mutableStateOf(
        MapUiSettings(
            zoomControlsEnabled = false,
            mapToolbarEnabled = false)
    ) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
            currentLocation?.latitude ?: 0.0,
                currentLocation?.longitude ?:  0.0), 9f)
    }

    val context = LocalContext.current
    var clusterManager by remember {
        mutableStateOf<ClusterManager<Plant>?>(null)
    }
    var clusterRenderer by remember {
        mutableStateOf<PlantDiscovererMapRenderer?>(null)
    }
    var lastClickedMarker by remember {
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
                    algorithm = GridBasedAlgorithm()

                    renderer.setOnClusterItemClickListener { item ->
                        // If there was a marker user clicked before, its
                        lastClickedMarker?.let {
                            it.marker.setIcon(
                                BitmapDescriptorFactory.fromBitmap(
                                    MarkerUtils.createCustomMarkerFromLayout(context, it.plant, false)
                                )
                            )
                            lastClickedMarker = null
                        }

                        // Now the clicked marker state is saved
                        lastClickedMarker = PlantMarker(item, clusterRenderer?.getMarker(item)!!)
                        lastClickedMarker?.marker?.setIcon(
                            BitmapDescriptorFactory.fromBitmap(
                                MarkerUtils.createCustomMarkerFromLayout(context, item!!, true)
                            )
                        )
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
    }
}
