package com.mr3y.thoughts.components.foundation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.graphics.Paint as nativePaint

const val DefaultNumOfRows = 4
const val DefaultNumOfCellsInOneRow = 8

@Composable
fun Graph(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                val points = generateRandomPoints(size)
                val path = Path().apply {
                    val bounds = points.first() to points.last()
                    moveTo(bounds.first.x, bounds.first.y)
                    lineTo(bounds.first.x, size.height)
                    lineTo(bounds.second.x, size.height)
                    points.subList(1, points.size).reversed().forEach {
                        lineTo(it.x, it.y)
                    }
                    close()
                }
                val cellSize = Size(size.width / DefaultNumOfCellsInOneRow, size.height / DefaultNumOfRows)
                onDrawBehind {
                    val paint = Paint().asFrameworkPaint().apply {
                        isAntiAlias = true
                        strokeWidth = 1f
                        strokeCap = nativePaint.Cap.ROUND
                        color = Color.Yellow.toArgb()
                        textSize = 32f
                    }
                    for (i in 0 until DefaultNumOfRows) {
                        for (j in 0 until DefaultNumOfCellsInOneRow) {
                            drawRect(
                                style = Stroke(2f),
                                color = Color.White.copy(alpha = 0.5f),
                                topLeft = Offset(x = j * cellSize.width, y = i * cellSize.height),
                                size = cellSize,
                            )
                            if (i == DefaultNumOfRows - 1) {
                                drawIntoCanvas {
                                    it.nativeCanvas.drawText("$j", j * cellSize.width, size.height, paint)
                                }
                            }
                        }
                        drawIntoCanvas {
                            it.nativeCanvas.drawText("${DefaultNumOfRows - i}", 0f, i * cellSize.height, paint)
                        }
                    }
                    drawPoints(
                        points = points,
                        pointMode = PointMode.Polygon,
                        color = Color.Green,
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(listOf(Color.Green.copy(0.5f), Color.White.copy(0.01f))),
                        blendMode = BlendMode.ColorBurn
                    )
                }
            }
    )
}

fun generateRandomPoints(size: Size): List<Offset> {
    return listOf(
        Offset(
            size.width * 0.1f, size.height * 0.2f
        ),
        Offset(
            size.width * 0.2f, size.height * 0.4f
        ),
        Offset(
            size.width * 0.3f, size.height * 0.3f
        ),
        Offset(
            size.width * 0.4f, size.height * 0.8f
        ),
        Offset(
            size.width * 0.6f, size.height * 0.1f
        ),
        Offset(
            size.width * 0.8f, size.height * 0.5f
        ),
    )
}

@Preview(widthDp = 260, heightDp = 280)
@Composable
fun GraphPreview() {
    Graph(modifier = Modifier.size(200.dp))
}
