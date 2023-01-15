package cz.mendelu.xmusil5.plantdiscoverer.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.utils.Constants
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.settingsDataStore
import kotlinx.coroutines.flow.first

class ImageRecognizerMock(): ImageRecognizing {

    override fun retriveImageFromUri(context: Context, uri: String): Bitmap?{
        val bitmap = Bitmap.createBitmap(
            200,
            200,
            Bitmap.Config.ARGB_8888
        )
        return bitmap
    }

    override suspend fun processImage(imageBitmap: Bitmap, onFinishedListener: (DetectedObject?) -> Unit) {
        onFinishedListener(DetectedObject(Rect(), 176, listOf(
            DetectedObject.Label("Aloe vera", 0.65f, 1),
            DetectedObject.Label("Aloe ferox", 0.35f, 2),
            DetectedObject.Label("Buxus confii", 0.25f, 3),
        )))
    }

    override suspend fun getConfidenceTreshold(): Float{
        return 0.01f
    }

    override suspend fun setConfidenceTreshold(newTreshold: Float){

    }
}