package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

@Composable
fun CustomOutlinedButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    textSize: TextUnit = 17.sp,
    onClick: () -> Unit,
    cornerRadius: Dp = 10.dp,
    modifier: Modifier = Modifier.fillMaxSize()
){
    OutlinedButton(
        content = {
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = textSize
            )
        },
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor),
        onClick = onClick,
        modifier = modifier
            .customShadow(color = shadowColor,
                borderRadius = cornerRadius,
                spread = 1.dp,
                blurRadius = 5.dp,
                offsetY = 5.dp
            )
    )
}