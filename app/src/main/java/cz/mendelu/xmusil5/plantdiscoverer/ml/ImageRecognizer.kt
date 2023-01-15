package cz.mendelu.xmusil5.plantdiscoverer.ml

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import cz.mendelu.xmusil5.plantdiscoverer.utils.Constants
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils
import cz.mendelu.xmusil5.plantdiscoverer.utils.settingsDataStore
import kotlinx.coroutines.flow.first

class ImageRecognizer(
    private val mlModelPath: String,
    private val context: Context
): ImageRecognizing {

    companion object {
        val defaultTreshold = 0.01f
    }

    private val model = LocalModel.Builder()
        .setAssetFilePath(mlModelPath)
        .build()


    override fun retriveImageFromUri(context: Context, uri: String): Bitmap?{
        return PictureUtils.getImageBitmapFromUri(context, uri)
    }

    override suspend fun processImage(imageBitmap: Bitmap, onFinishedListener: (DetectedObject?) -> Unit) {
        val confidenceTreshold = getConfidenceTreshold()

        val singleImageOptions = CustomObjectDetectorOptions.Builder(model)
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .setClassificationConfidenceThreshold(confidenceTreshold)
            .enableClassification()
            .build()

        val imageDetector = ObjectDetection.getClient(singleImageOptions)

        val input = InputImage.fromBitmap(imageBitmap, 0)
        imageDetector.process(input).addOnSuccessListener {
            val detected = it.firstOrNull()
            if (detected != null){
                detected.labels.removeIf { it.text == "None" }
                onFinishedListener(detected)
            } else {
                onFinishedListener(null)
            }
        }.addOnFailureListener{
            onFinishedListener(null)
        }.addOnCanceledListener {
            onFinishedListener(null)
        }
    }

    override suspend fun getConfidenceTreshold(): Float{
        val preference = context.settingsDataStore.data.first()
        return preference[floatPreferencesKey(Constants.CONFIDENCE_TRESHOLD_KEY)] ?: defaultTreshold
    }

    override suspend fun setConfidenceTreshold(newTreshold: Float){
        if (newTreshold in 0.01f..1.0f){
            context.settingsDataStore.edit {
                it[floatPreferencesKey(Constants.CONFIDENCE_TRESHOLD_KEY)] = newTreshold
            }
        }
        else throw java.lang.IllegalArgumentException("ML treshold must be between 0.01f and 100f")
    }
}