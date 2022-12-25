package cz.mendelu.xmusil5.plantdiscoverer.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import cz.mendelu.xmusil5.plantdiscoverer.activities.MainActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

fun checkCameraPermission(context: Context): Boolean{
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        return true
    }
    return false
}

@Composable
fun LazyGridState.onLastReached(loadMore: () -> Unit){
    val shouldLoad = remember{
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }
    LaunchedEffect(shouldLoad){
        snapshotFlow { shouldLoad.value }
            .collect {
                if (it) { loadMore() }
            }
    }
}