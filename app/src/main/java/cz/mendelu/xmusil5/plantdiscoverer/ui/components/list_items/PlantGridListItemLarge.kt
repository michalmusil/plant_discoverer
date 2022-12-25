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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.model.database_entities.Plant
import cz.mendelu.xmusil5.plantdiscoverer.utils.PictureUtils

@Composable
fun PlantGridListItemLarge(
    plant: Plant,
    onItemClick: (Plant) -> Unit
){
    val photoBitmap = PictureUtils.fromByteArrayToBitmap(plant.photo)
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .padding(5.dp)
            .background(color = MaterialTheme.colorScheme.tertiary, RoundedCornerShape(10.dp))
            .clickable {
                onItemClick(plant)
            }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                bitmap = photoBitmap?.asImageBitmap() ?: ImageBitmap.imageResource(id = R.drawable.ic_error),
                contentDescription = plant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp)))
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 3.dp)
            ) {
                Text(
                    text = plant.name,
                    fontSize = 14.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onTertiary,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )
                Text(
                    text = plant.description ?: "",
                    fontSize = 12.sp,
                    maxLines = 3,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onTertiary,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
        }


    }
}