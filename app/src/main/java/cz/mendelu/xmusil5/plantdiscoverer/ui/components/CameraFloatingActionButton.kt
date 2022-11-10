package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import android.Manifest
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.activities.MainActivity
import cz.mendelu.xmusil5.plantdiscoverer.utils.checkCameraPermission

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraFloatingActionButton(
    modifier: Modifier,
    onSuccessfullCameraClick: () -> Unit
){
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA
        )
    )
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        shape = CircleShape,
        modifier = modifier,
        onClick = {
            permissionState.permissions.forEach {
                if (it.permission == Manifest.permission.CAMERA){
                    when {
                        it.hasPermission -> {
                            onSuccessfullCameraClick()
                        }
                        it.shouldShowRationale -> {
                            permissionState.launchMultiplePermissionRequest()
                            // TODO - Show some kind of dialog that tells user, that camera permission is necessary
                        }
                        !it.hasPermission && !it.shouldShowRationale -> {
                            permissionState.launchMultiplePermissionRequest()
                            // TODO - Show dialog telling user that he has to go to settings and enable camera permission
                        }
                    }
                }
            }
        })
    {
        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = stringResource(id = R.string.floatingActionButton,)
        )
    }
}