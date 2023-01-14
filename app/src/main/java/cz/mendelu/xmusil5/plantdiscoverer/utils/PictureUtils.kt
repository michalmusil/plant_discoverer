package cz.mendelu.xmusil5.plantdiscoverer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.ui.screens.new_plant_screen.NewPlantUiState
import java.io.ByteArrayOutputStream
import java.io.File


object PictureUtils {

    fun fromBitmapToByteArray(bitmap: Bitmap?): ByteArray?{
        if(bitmap != null) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
            return outputStream.toByteArray()
        } else{
            return null
        }
    }

    fun fromByteArrayToBitmap(byteArray: ByteArray?, compressionQuality: Int? = null): Bitmap?{
        if(byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            compressionQuality?.let { quality ->
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                val array = outputStream.toByteArray()
                return BitmapFactory.decodeByteArray(array, 0, array.size)
            }
            return bitmap
        } else{
            return null
        }
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    fun getImageBitmapFromUri(context: Context, uriString: String): Bitmap?{
        try {
            val uri = Uri.parse(uriString)
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            var bitmapRotated: Bitmap? = null

            // Rotating the image to be upright
            // based on how it's stored in file system - orientation stored in exif tag
            val exif = ExifInterface(uri.path.toString())
            val photoOrientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            val rotationDegrees: Float = when(photoOrientation){
                3 -> 180.0f
                4 -> 180.0f
                5 -> 90.0f
                6 -> 90.0f
                7 -> 270.0f
                8 -> 270.0f
                else -> 0.0f
            }
            val matrix = Matrix().apply {
                postRotate(rotationDegrees)
            }
            bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

            try {
                val photoToDelete = File(uri.path)
                if (photoToDelete.exists()){
                    photoToDelete.delete()
                }
            }
            catch (ex: java.lang.Exception){
                // Nothing for now
            }
            return bitmapRotated

        } catch (ex: java.lang.Exception){
            return null
        }
    }
}