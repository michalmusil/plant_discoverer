package cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

const val TAG_PLANT_IMAGE_GRID_LIST_ITEM = "plantImageGridListItem"

@Composable
fun PlantImageGridListItem(imageAddress: String, onClick: () -> Unit){
    val context = LocalContext.current
    val cornerRadius = 8.dp
    Box(
        modifier = Modifier
            .testTag(TAG_PLANT_IMAGE_GRID_LIST_ITEM)
            .width(145.dp)
            .aspectRatio(1f)
            .padding(5.dp)
            .clickable {
                onClick()
            }
    ){
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageAddress)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.plantImage),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .customShadow(color = shadowColor,
                    borderRadius = cornerRadius,
                    spread = 1.dp,
                    blurRadius = 5.dp,
                    offsetY = 2.dp
                )
                .clip(RoundedCornerShape(8.dp))
        )
    }
}