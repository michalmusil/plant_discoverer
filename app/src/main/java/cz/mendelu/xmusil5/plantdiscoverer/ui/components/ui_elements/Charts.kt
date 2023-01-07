package cz.mendelu.xmusil5.plantdiscoverer.ui.components.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantdiscoverer.model.code_models.Month
import cz.mendelu.xmusil5.plantdiscoverer.ui.theme.shadowColor
import cz.mendelu.xmusil5.plantdiscoverer.utils.customShadow

@Composable
fun MonthlyColumnChart(
    title: String,
    data: Map<Month, Double>,
    columnColor: Color = MaterialTheme.colorScheme.primary,
    scaleColor: Color = MaterialTheme.colorScheme.onBackground,
    height: Dp = 300.dp,
    cornerRadius: Dp = 12.dp
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val columnWidth = (screenWidth*0.046).dp
    val columnSpacing = (screenWidth*0.02).dp
    // BarGraph Dimensions
    val barGraphHeight by remember { mutableStateOf(height) }
    val barColumnWidth by remember { mutableStateOf(columnWidth) }
    val barColumnSpacingWidth by remember { mutableStateOf(columnSpacing) }
    // Scale Dimensions
    val scaleYAxisWidth by remember { mutableStateOf(20.dp) }
    val scaleLineWidth by remember { mutableStateOf(2.dp) }
    // Other important values
    val maxValue = data.maxOf {
        it.value
    }
    val yScaleFontSize = 13.sp
    val xScaleFontSize = 11.sp


    // Y axis values
    val topScaleValue = data.maxOf {
        it.value
    }.toInt()
    val secondTopScaleValue = (topScaleValue * 0.75).toInt()
    val middleScaleValue = (topScaleValue * 0.5).toInt()
    val bottomScaleValue = (topScaleValue * 0.25).toInt()

    val yAxisValues = mutableListOf(topScaleValue, secondTopScaleValue, middleScaleValue, bottomScaleValue)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .customShadow(
                color = shadowColor,
                borderRadius = cornerRadius,
                spread = 1.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Title row
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Y axis scale and columns row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            // Y-Axis scale
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleYAxisWidth),
                verticalArrangement = Arrangement.Top
            ) {
                yAxisValues.forEach {
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ){
                        Text(
                            text = it.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = yScaleFontSize,
                            color = scaleColor,
                            modifier = Modifier
                        )
                    }
                }
            }

            // Y-Axis line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleLineWidth)
                    .background(scaleColor)
            )

            // Graph (columns)
            for (i in 1..data.size){
                val month = Month.getByOrder(i)
                month?.let {
                    val monthValue = data.get(it)
                    monthValue?.let {
                        val columnHeight = (barGraphHeight.value*(monthValue/maxValue)).dp
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier
                                .padding(start = barColumnSpacingWidth)
                                .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                                .width(barColumnWidth)
                                .height(columnHeight)
                                .background(columnColor)
                        ){
                            Text(
                                text = monthValue.toInt().toString(),
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                lineHeight = 13.sp,
                                modifier = Modifier
                                    .padding(top = 5.dp)
                            )
                        }
                    }
                }
            }

        }

        // X-Axis line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(scaleLineWidth)
                .background(scaleColor)
        )

        // X-Axis scale row
        Row(
            modifier = Modifier
                .padding(start = scaleYAxisWidth + scaleLineWidth)
                .fillMaxWidth()
        ) {

            for (i in 1..data.size){
                val month = Month.getByOrder(i)
                month?.let {
                    val monthValue = data.get(it)
                    monthValue?.let {
                        Text(
                            modifier = Modifier
                                .padding(start = barColumnSpacingWidth)
                                .width(barColumnWidth),
                            text = month.order.toString(),
                            textAlign = TextAlign.Center,
                            fontSize = xScaleFontSize,
                            color = scaleColor
                        )
                    }
                }
            }
        }

    }

}







