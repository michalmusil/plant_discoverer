package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.navigation.Destination
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.templates.BottomNavItem
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.templates.BottomNavigationBar

const val TAG_BOTTOM_NAV_PLANT_LIST = "bottomNavPlantList"
const val TAG_BOTTOM_NAV_HOME = "bottomNavHome"
const val TAG_BOTTOM_NAV_MAP = "bottomNavMap"
const val TAG_NAVIGATION_BACK_ARROW = "navigationBackArrow"



@Composable
fun ScreenSkeleton(
    topBarText: String,
    navigation: INavigationRouter,
    content: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    showBackArrow: Boolean = false,
    onBackClick: () -> Unit = {},
    scaffoldState: ScaffoldState = rememberScaffoldState()) {

    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = topBarText,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(start = 0.dp)
                                .weight(1.5f)
                        )
                    }
                },
                actions = actions,
                navigationIcon = if (showBackArrow) {
                    {
                        IconButton(onClick = onBackClick, modifier = Modifier.testTag(
                            TAG_NAVIGATION_BACK_ARROW)) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.forward),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                } else {
                    null
                },
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navigation.getNavController(),
                items = listOf(
                    BottomNavItem(stringResource(id = R.string.plantsList), ImageVector.vectorResource(id = R.drawable.ic_grid), Destination.PlantsListScreen, Modifier.testTag(
                        TAG_BOTTOM_NAV_PLANT_LIST)),
                    BottomNavItem(stringResource(id = R.string.home), ImageVector.vectorResource(id = R.drawable.ic_hub), Destination.HomeScreen, Modifier.testTag(
                        TAG_BOTTOM_NAV_HOME)),
                    BottomNavItem(stringResource(id = R.string.map), ImageVector.vectorResource(id = R.drawable.ic_globe), Destination.MapScreen, Modifier.testTag(
                        TAG_BOTTOM_NAV_MAP))
                ),
                onItemClick = {
                    navigation.getNavController().navigate(it.destination.route)
                })
        }
    ) {
        Box(
            modifier = Modifier
                .background(color = androidx.compose.material3.MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(it)
        ){
            content()
        }
    }
}