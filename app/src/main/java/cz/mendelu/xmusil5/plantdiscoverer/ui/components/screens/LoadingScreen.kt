package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.PlantDiscovererTheme
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.secondary

@Composable
fun LoadingScreen(){
    val sizeLogo = 200.dp
    val sizeIndicator = 215.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(sizeIndicator),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 10.dp)
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_pd_logo),
            contentDescription = stringResource(id = R.string.appLogo),
            modifier = Modifier
                .size(sizeLogo)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    PlantDiscovererTheme {
        LoadingScreen()
    }
}