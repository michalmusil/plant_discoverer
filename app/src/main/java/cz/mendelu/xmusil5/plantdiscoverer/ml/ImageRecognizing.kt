package cz.mendelu.xmusil5.plantdiscoverer.ml

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.objects.DetectedObject

interface ImageRecognizing {
    fun retriveImageFromUri(context: Context, uri: String): Bitmap?

    suspend fun processImage(imageBitmap: Bitmap, onFinishedListener: (DetectedObject?) -> Unit)

    suspend fun getConfidenceTreshold(): Float

    suspend fun setConfidenceTreshold(newTreshold: Float)
}