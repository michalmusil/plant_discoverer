package cz.mendelu.xmusil5.plantdiscoverer.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.lifecycle.ProcessCameraProvider
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