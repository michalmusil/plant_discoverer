package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.utils.LanguageUtils
import org.koin.androidx.compose.getViewModel
import java.util.*
import kotlin.math.exp

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel = getViewModel()
) {
    ScreenSkeleton(
        topBarText = stringResource(id = R.string.settings),
        navigation = navigation,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                SettingsScreenContent(navigation = navigation, viewModel = viewModel)
            }
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
                SettingsItems(
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun SettingsItems(
    viewModel: SettingsViewModel
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {

        val selectedLanguage = rememberSaveable {
            mutableStateOf(
                LanguageUtils.Language.getByCodeDefaultEnglish(Locale.getDefault().language)
            )
        }

        LanguageOptions(
            items = LanguageUtils.Language.values().toList(),
            selectedItem = selectedLanguage,
            onItemClick = {
                viewModel.setAppLanguage(it){
                    selectedLanguage.value = it
                }
            }
        )
        Divider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp,
            modifier = Modifier
                .padding(vertical = 15.dp)
        )
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

@Composable
fun LanguageOptions(
    items: List<LanguageUtils.Language>,
    selectedItem: MutableState<LanguageUtils.Language>,
    onItemClick: (LanguageUtils.Language) -> Unit,
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.appLanguage),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 10.dp, start = 3.dp)

        )

        var expanded = rememberSaveable() {
            mutableStateOf(false)
        }

        OutlinedButton(
            onClick = {
                expanded.value = true
            },
            colors = ButtonDefaults.outlinedButtonColors(MaterialTheme.colorScheme.surface),
        ) {
            Text(
                text = selectedItem.value.originName,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null
            )
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                }) {
                items.forEach {
                    DropdownMenuItem(onClick = {
                        expanded.value = false
                        onItemClick(it)
                    }) {
                        Text(text = it.originName)
                    }
                }
            }
        }
    }
}