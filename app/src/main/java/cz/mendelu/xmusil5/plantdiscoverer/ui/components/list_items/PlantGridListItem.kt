package cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils

@Composable
fun PlantGridListItem(
    plant: Plant,
    onItemClick: (Plant) -> Unit
){
    val photoBitmap = PictureUtils.fromByteArrayToBitmap(plant.photo)
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .padding(3.dp)
            .clickable {
                onItemClick(plant)
            }
    ){
        Image(
            bitmap = photoBitmap?.asImageBitmap() ?: PictureUtils.getBitmapFromVectorDrawable(
                LocalContext.current, R.drawable.ic_error)!!.asImageBitmap(),
            contentDescription = "desc",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(5.dp)
                .clip(RoundedCornerShape(8.dp)))
        Text(
            text = plant.name,
            fontSize = 16.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(9.dp))
                .background(color = MaterialTheme.colorScheme.secondary, shape = RectangleShape)
                .padding(horizontal = 5.dp),
            overflow = TextOverflow.Ellipsis

        )
    }
}