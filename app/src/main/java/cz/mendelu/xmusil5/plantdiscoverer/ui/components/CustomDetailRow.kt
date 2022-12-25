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

@Composable
fun CustomDetailRow(
    title: String,
    text: String,
    iconId: Int,
    accentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(horizontal = 7.dp)
                .padding(top = 3.dp)
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
                    color = accentColor,
                    fontSize = 15.sp
                )
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = androidx.compose.ui.graphics.Color.Black,
                    lineHeight = 17.sp
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
    accentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(horizontal = 7.dp)
                .padding(top = 3.dp)
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
                        color = accentColor,
                        fontSize = 15.sp
                    )
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Black
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
                    color = androidx.compose.ui.graphics.Color.Black,
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
    additionalButtonIconId: Int,
    onButtonClick: () -> Unit,
    accentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(9.dp))
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(horizontal = 7.dp)
                .padding(top = 3.dp)
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
                        color = accentColor,
                        fontSize = 15.sp
                    )
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Black
                    )
                }
            }
            Column (
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = onButtonClick) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = additionalButtonIconId),
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            }
        }
    }
}