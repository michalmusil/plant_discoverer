package cz.mendelu.xmusil5.plantdiscoverer.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.imageview.ShapeableImageView
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils

object MarkerUtils {

    fun createCustomMarkerFromLayout(
        context: Context,
        plant: Plant,
        makeLarge: Boolean,
        markerColor: Color
    ): Bitmap {
        val layout = if (makeLarge) R.layout.plant_marker_large else R.layout.plant_marker
        val markerView = LayoutInflater.from(context).inflate(layout, null)

        val markerImage = markerView.findViewById<ShapeableImageView>(R.id.marker_image)
        val markerBackground = markerView.findViewById<ImageView>(R.id.marker_background)

        val plantPhoto = PictureUtils.fromByteArrayToBitmap(plant.photo, compressionQuality = 10)
        plantPhoto?.let {
            markerImage.setImageDrawable(it.toDrawable(context.resources))
        }

        markerBackground?.let {
            it.setColorFilter(markerColor.toArgb(), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0, 0,
            markerView.getMeasuredWidth(),
            markerView.getMeasuredHeight()
        )
        markerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            markerView.getMeasuredWidth(), markerView.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        val drawable: Drawable = markerView.getBackground()
        drawable.draw(canvas)
        markerView.draw(canvas)
        return returnedBitmap
    }
}