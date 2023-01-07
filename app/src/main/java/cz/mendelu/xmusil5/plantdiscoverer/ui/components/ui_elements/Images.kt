package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

@Composable
fun BigImage(
    photo: ImageBitmap,
    aspectRatio: Float = 1f,
    contentDescription: String,
    onClick: () -> Unit = {},
){
    val cornerRadius = 20.dp
    Image(
        bitmap = photo,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .customShadow(
                color = shadowColor,
                borderRadius = cornerRadius,
                spread = 3.dp,
                blurRadius = 7.dp,
                offsetY = 3.dp
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            }
    )
}

@Composable
fun BigImageWithText(
    photo: ImageBitmap,
    aspectRatio: Float = 1f,
    contentDescription: String,
    titleText: String,
    secondaryText: String? = null,
    onClick: () -> Unit = {}
){
    val cornerRadius = 20.dp
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {
        Image(
            bitmap = photo,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .customShadow(
                    color = shadowColor,
                    borderRadius = cornerRadius,
                    spread = 3.dp,
                    blurRadius = 7.dp,
                    offsetY = 3.dp
                )
                .clip(RoundedCornerShape(20.dp))
        )
        Text(
            text = titleText,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(15.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(6.dp)
        )

        secondaryText?.let {
            Text(
                text = secondaryText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(5.dp)
            )
        }
    }
}

