package cz.mendelu.xmusil5.plantdiscoverer.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.flow.first

class ImageReckognizer(
    private val mlModelPath: String,
    private val context: Context
) {

    companion object {
        val defaultTreshold = 0.01f
    }

    private val model = LocalModel.Builder()
        .setAssetFilePath(mlModelPath)
        .build()


    suspend fun processImage(imageBitmap: Bitmap, onFinishedListener: (DetectedObject?) -> Unit) {
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
            detected?.let{
                detected.labels.forEach {
                    if (it.text == "None"){
                        // FOR TESTING
                        /*
                        detected.labels.add(DetectedObject.Label("Aloe vera", 0.95f, 2))
                        detected.labels.add(DetectedObject.Label("Buxus branti", 0.71f, 3))
                        detected.labels.add(DetectedObject.Label("Cofilae venti", 0.48f, 4))
                        detected.labels.add(DetectedObject.Label("Pampolabea maxima", 0.18f, 5))
                         */

                        detected.labels.remove(it)
                    }
                }
            }
            onFinishedListener(detected)
        }.addOnFailureListener{
            onFinishedListener(null)
        }
    }

    suspend fun getConfidenceTreshold(): Float{
        val preference = context.settingsDataStore.data.first()
        return preference[floatPreferencesKey(Constants.CONFIDENCE_TRESHOLD_KEY)] ?: defaultTreshold
    }

    suspend fun setConfidenceTreshold(newTreshold: Float){
        if (newTreshold in 0.01f..1.0f){
            context.settingsDataStore.edit {
                it[floatPreferencesKey(Constants.CONFIDENCE_TRESHOLD_KEY)] = newTreshold
            }
        }
        else throw java.lang.IllegalArgumentException("ML treshold must be between 0.01f and 100f")
    }
}