package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import android.Manifest
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import cz.mendelu.xmusil5.plantdiscoverer.R

const val TAG_CAMERA_FAB = "cameraFloatingActionButton"

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

    val hasBeenClicked: MutableState<Boolean> = remember{
        mutableStateOf(false)
    }

    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        shape = CircleShape,
        modifier = modifier
            .testTag(TAG_CAMERA_FAB),
        onClick = {
            hasBeenClicked.value = true
            permissionState.permissions.forEach {
                if (it.permission == Manifest.permission.CAMERA){
                    when {
                        it.shouldShowRationale -> {
                            permissionState.launchMultiplePermissionRequest()
                        }
                        !it.hasPermission && !it.shouldShowRationale -> {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }
                }
            }
        })
    {
        if (hasBeenClicked.value == true && permissionState.allPermissionsGranted){
            hasBeenClicked.value = false
            onSuccessfullCameraClick()
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = stringResource(id = R.string.floatingActionButton,)
        )
    }
}