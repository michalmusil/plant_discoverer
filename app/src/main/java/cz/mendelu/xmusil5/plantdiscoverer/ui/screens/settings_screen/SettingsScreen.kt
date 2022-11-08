package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = "Settings",
        navigation = navigation,
        content = {
            SettingsScreenContent(navigation = navigation, viewModel = viewModel)
        }
    )
}

@Composable
fun SettingsScreenContent(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel
){
    viewModel.settingsUiState.value.let {
        when(it){
            is SettingsUiState.Start -> {
                SettingsItems()
            }
        }
    }
}

@Composable
fun SettingsItems(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SettingsClickableItem(
            title = stringResource(id = R.string.appLanguage),
            value = "zatim nic"
        ) {
            //do something
        }
        SettingsClickableItem(
            title = stringResource(id = R.string.dateTimeFormat),
            value = "zatim nic2"
        ) {
            //do something
        }
        Divider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp,
            modifier = Modifier
                .padding(vertical = 15.dp)
        )
        SettingsSwitchItem(
            title = stringResource(id = R.string.useQuickAddition),
            value = stringResource(id = R.string.useQuickAdditionDescribtion),
            checked = false,
            onSwitch = {
                // do something on item switch
            })
    }
}

@Composable
fun SettingsClickableItem(
    title: String,
    value: String,
    onItemClick: () -> Unit
){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick()
            }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    value: String,
    checked: Boolean,
    onSwitch: (Boolean) -> Unit
){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(2f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
            )
        }

        val checked = remember{
            mutableStateOf(checked)
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
        ) {
            Switch(checked = checked.value, onCheckedChange = {
                checked.value = !checked.value
                onSwitch(it)
            })
        }

    }
}