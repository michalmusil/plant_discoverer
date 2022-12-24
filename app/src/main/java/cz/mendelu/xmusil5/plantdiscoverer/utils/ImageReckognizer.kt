package cz.mendelu.xmusil5.plantdiscoverer.utils

import android.graphics.Bitmap
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

class ImageReckognizer(
    private val mlModelPath: String,
    private val acceptedReckognitionConfidence: Float = 0.1f
) {

    private val model = LocalModel.Builder()
        .setAssetFilePath(mlModelPath)
        .build()

    private val singleImageOptions = CustomObjectDetectorOptions.Builder(model)
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .setClassificationConfidenceThreshold(acceptedReckognitionConfidence)
        .enableClassification()
        .build()

    private val imageDetector = ObjectDetection.getClient(singleImageOptions)

    fun processImage(imageBitmap: Bitmap, onFinishedListener: (List<DetectedObject>) -> Unit) {
        val input = InputImage.fromBitmap(imageBitmap, 0)
        imageDetector.process(input).addOnSuccessListener {
            onFinishedListener(it)
        }.addOnFailureListener{
            onFinishedListener(listOf())
        }
    }
}