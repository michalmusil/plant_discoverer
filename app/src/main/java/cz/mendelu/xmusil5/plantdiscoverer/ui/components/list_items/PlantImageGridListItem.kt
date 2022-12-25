package cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.mendelu.xmusil5.plantdiscoverer.R

@Composable
fun PlantImageGridListItem(imageAddress: String){
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(145.dp)
            .aspectRatio(1f)
            .padding(5.dp)
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
                .clip(RoundedCornerShape(8.dp))
        )
    }
}