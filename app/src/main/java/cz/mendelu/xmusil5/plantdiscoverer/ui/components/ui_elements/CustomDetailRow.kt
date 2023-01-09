package cz.mendelu.xmusil5.plantdiscoverer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.R
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

@Composable
fun CustomDetailRow(
    title: String,
    text: String,
    iconId: Int,
    accentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    valueModifier: Modifier = Modifier
){
    val cornerRadius = 9.dp
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .customShadow(color = shadowColor,
                    borderRadius = cornerRadius,
                    spread = 1.dp,
                    blurRadius = 5.dp,
                    offsetY = 2.dp
                )
                .clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 7.dp)
                .padding(vertical = 3.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = iconId),
                contentDescription = stringResource(id = R.string.plantImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(55.dp)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(accentColor)
                    .padding(7.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 5.dp),
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 15.sp
                )
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 17.sp,
                    modifier = valueModifier
                )
            }
        }
    }
}

@Composable
fun CustomDetailRowWithAdditionalLabel(
    title: String,
    text: String,
    additionalLabel: String,
    iconId: Int,
    accentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    valueModifier: Modifier = Modifier
){
    val cornerRadius = 9.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .customShadow(color = shadowColor,
                    borderRadius = cornerRadius,
                    spread = 1.dp,
                    blurRadius = 5.dp,
                    offsetY = 2.dp
                )
                .clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 7.dp)
                .padding(vertical = 3.dp)
        ) {
            Row {
                Image(
                    imageVector = ImageVector.vectorResource(id = iconId),
                    contentDescription = stringResource(id = R.string.plantImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(55.dp)
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(accentColor)
                        .padding(7.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp)
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 15.sp
                    )
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = valueModifier
                    )
                }
            }
            Column (
                modifier = Modifier
                    .padding(top = 17.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = additionalLabel,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun CustomDetailRowWithAdditionalButton(
    title: String,
    text: String,
    iconId: Int,
    buttonText: String,
    onButtonClick: () -> Unit,
    accentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    valueModifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier
){
    val cornerRadius = 9.dp
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .customShadow(color = shadowColor,
                    borderRadius = cornerRadius,
                    spread = 1.dp,
                    blurRadius = 5.dp,
                    offsetY = 2.dp
                )
                .clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 15.dp)
                .padding(vertical = 3.dp)
        ) {
            Row {
                Image(
                    imageVector = ImageVector.vectorResource(id = iconId),
                    contentDescription = stringResource(id = R.string.plantImage),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(55.dp)
                        .padding(vertical = 6.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(accentColor)
                        .padding(7.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 15.sp
                    )
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = valueModifier
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            ){
                CustomOutlinedButton(
                    text = buttonText,
                    textSize = 13.sp,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    onClick = {
                        onButtonClick()
                    },
                    modifier = buttonModifier
                        .fillMaxWidth()
                )
            }
        }
    }
}