package cz.mendelu.xmusil5.plantdiscoverer.utils

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageReckognizer(
    var acceptedReckognitionConfidence: Float = 0.5f
) {

    private val imageLabeler: ImageLabeler = ImageLabeling.getClient(ImageLabelerOptions
        .Builder()
        .setConfidenceThreshold(acceptedReckognitionConfidence)
        .build())


    fun processImage(imageBitmap: Bitmap, onFinishedListener: (ImageLabel?) -> Unit) {
        val input = InputImage.fromBitmap(imageBitmap, 0)
        imageLabeler.process(input).addOnSuccessListener {
            onFinishedListener(it.firstOrNull())
        }.addOnFailureListener{
            onFinishedListener(null)
        }
    }
}