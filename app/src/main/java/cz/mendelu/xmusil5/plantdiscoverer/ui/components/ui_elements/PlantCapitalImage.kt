package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

@Composable
fun BigSquareImage(
    photo: ImageBitmap,
    contentDescription: String,
){
    val cornerRadius = 20.dp
    Image(
        bitmap = photo,
        contentDescription = stringResource(id = R.string.plantImage),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .customShadow(color = shadowColor,
                borderRadius = cornerRadius,
                spread = 3.dp,
                blurRadius = 7.dp,
                offsetY = 3.dp
            )
            .clip(RoundedCornerShape(20.dp))
    )
}

