package cz.mendelu.xmusil5.plantdiscoverer.ui.components.list_items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.objects.DetectedObject
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

const val TAG_IMAGE_LABEL = "imageLabel"

@Composable
fun ImageLabelListItem(
    label: DetectedObject.Label,
    selectedLabel: MutableState<DetectedObject.Label?>,
    onClick: () -> Unit
){
    val name = label.text
    val confidence = (label.confidence*100).toInt().toString()
    val cornerRadius = 16.dp

    Box(
        Modifier
            .padding(horizontal = 4.dp)
            .customShadow(color = shadowColor,
                borderRadius = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = if (label == selectedLabel.value) { MaterialTheme.colorScheme.secondary }
                else { MaterialTheme.colorScheme.background },
            )
            .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(cornerRadius))
            .clickable {
                onClick()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag(TAG_IMAGE_LABEL)
            )
            Text(
                text = "${confidence}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}