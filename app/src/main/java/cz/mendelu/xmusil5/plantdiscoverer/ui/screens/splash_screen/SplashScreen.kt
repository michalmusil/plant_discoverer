package cz.mendelu.xmusil5.plantdiscoverer.ui.screens.splash_screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.navigation.Destination
import cz.mendelu.xmusil5.plantdiscoverer.navigation.INavigationRouter
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigation: INavigationRouter,
    startDestination: Destination,
    screenDuration: Long = 2000
){
    val targetLogoSize = remember {
        200f
    }
    val currentLogoSize = remember {
        Animatable(0f)
    }
    LaunchedEffect(true){
        currentLogoSize.animateTo(
            targetValue = targetLogoSize,
            animationSpec = tween(
                durationMillis = (screenDuration/2).toInt(),
                easing = {
                    OvershootInterpolator(1.5f).getInterpolation(it)
                }
            )
        )
    }
    LaunchedEffect(true){
        delay(screenDuration)
        navigation.getNavController().navigate(startDestination.route )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_pd_logo),
            contentDescription = stringResource(id = R.string.appLogo),
            modifier = Modifier
                .size(currentLogoSize.value.dp)
        )
    }
}