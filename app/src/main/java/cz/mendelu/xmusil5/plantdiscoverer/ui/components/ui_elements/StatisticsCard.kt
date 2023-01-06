package cz.mendelu.xmusil5.plantdiscoverer.ui.components.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

@Composable
fun StatisticsCard(
    label: String,
    value: String,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
    modifier: Modifier
){
    val cornerRadius = 12.dp
    val textSize = when{
        value.length < 4 -> { 28.sp }
        value.length < 6 -> { 23.sp }
        else -> { 20.sp }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 10.dp)
            .customShadow(color = shadowColor,
            borderRadius = cornerRadius,
            spread = 2.dp,
            blurRadius = 8.dp,
            offsetY = 3.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .clickable {
                onClick()
            }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(15.dp)
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                textAlign = TextAlign.Center,
                color = textColor,
                modifier = Modifier
                    .padding(bottom = 5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontSize = textSize,
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
    }
}