package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.activities.MainActivity

@Composable
fun CameraFloatingActionButton(
    modifier: Modifier,
    onSuccessfullCameraClick: () -> Unit
){
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        shape = CircleShape,
        modifier = modifier,
        onClick = {
            if (MainActivity.cameraPermited){
                // GO TO OPENING A CAMERA
                onSuccessfullCameraClick
            } else{
                // SHOW SOME KING OF DIALOG
                print("y")
            }
        })
    {
        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = stringResource(id = R.string.floatingActionButton,)
        )
    }
}