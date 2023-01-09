package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.settings_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.LoadingScreen
import cz.mendelu.xmusil5.plantdiscoverer.ui.components.ScreenSkeleton
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.LanguageUtils
import org.koin.androidx.compose.getViewModel
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel = getViewModel()
) {

    // IMPORTANT!!! - Defined here so that settings screen recomposes after language is changed
    val appLanguage = remember{
        mutableStateOf(LanguageUtils.Language.getByCodeDefaultEnglish(Locale.getDefault().language))
    }
    if (appLanguage.value != null) {
        ScreenSkeleton(
            topBarText = stringResource(id = R.string.settings),
            navigation = navigation,
            showBackArrow = true,
            onBackClick = {
                navigation.returnBack()
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    SettingsScreenContent(
                        navigation = navigation,
                        viewModel = viewModel,
                        appLanguage = appLanguage
                    )
                }
            }
        )
    }
}

@Composable
fun SettingsScreenContent(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel,
    appLanguage: MutableState<LanguageUtils.Language>
){
    viewModel.settingsUiState.value.let {
        when(it){
            is SettingsUiState.Start -> {
                LaunchedEffect(it){
                    viewModel.loadConfidenceTreshold()
                }
                LoadingScreen()
            }
            is SettingsUiState.DataLoaded -> {
                SettingsItems(
                    viewModel = viewModel,
                    it.mlConfidenceTreshold,
                    appLanguage
                )
            }
        }
    }
}

@Composable
fun SettingsItems(
    viewModel: SettingsViewModel,
    currentConfidenceTreshold: Int,
    appLanguage: MutableState<LanguageUtils.Language>
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        val context = LocalContext.current

        LanguageOptions(
            items = LanguageUtils.Language.values().toList(),
            selectedItem = appLanguage,
            onItemClick = {
                viewModel.setAppLanguage(context, it){
                    appLanguage.value = it
                }
            }
        )


        Divider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp,
            modifier = Modifier
                .padding(vertical = 15.dp)
        )


        val currentTreshold = rememberSaveable {
            mutableStateOf(currentConfidenceTreshold)
        }
        TresholdChoice(
            viewModel = viewModel,
            currentValue = currentTreshold)
    }
}

@Composable
fun TresholdChoice(
    viewModel: SettingsViewModel,
    currentValue: MutableState<Int>,
){
    val cornerRadius = 5.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 3.dp)
        ) {
            Text(
                text = stringResource(id = R.string.mlMinimalConfidenceTreshold),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "${currentValue.value} %",
                style = MaterialTheme.typography.titleSmall
            )
        }

        Slider(
            value = currentValue.value.toFloat(),
            onValueChange = {
                currentValue.value = it.toInt()
            },
            onValueChangeFinished = {
                viewModel.setNewConfidenceTreshold(currentValue.value)
            },
            valueRange = 1f..100f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTickColor = shadowColor
            ),
        )
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .offset(y = -10.dp)
        ) {
            Text(
                text = "1 %",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "100 %",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun LanguageOptions(
    items: List<LanguageUtils.Language>,
    selectedItem: MutableState<LanguageUtils.Language>,
    onItemClick: (LanguageUtils.Language) -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.appLanguage),
            style = MaterialTheme.typography.titleSmall,
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
            shape = RoundedCornerShape(5.dp),
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