package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.navigation.Destination
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.disabled

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val destination: Destination
)


@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItem>,
    onItemClick: (navItem: BottomNavItem) -> Unit
){
    val currentBackStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary
    ) {
        items.forEach { navItem ->
            BottomNavigationItem(
                selected = currentBackStackEntry.value?.destination?.route == navItem.destination.route,
                selectedContentColor = MaterialTheme.colorScheme.secondary,
                unselectedContentColor = disabled,
                onClick = { onItemClick(navItem) },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(imageVector = navItem.icon, contentDescription = navItem.title)
                        if (currentBackStackEntry.value?.destination?.route == navItem.destination.route){
                            Text(
                                text = navItem.title,
                                textAlign = TextAlign.Center,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            )
        }
    }
}