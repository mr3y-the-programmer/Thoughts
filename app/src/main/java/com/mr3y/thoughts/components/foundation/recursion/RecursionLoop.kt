package com.mr3y.thoughts.components.foundation.recursion

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val startColor = Color(0xFFf06292)
val midColor = Color(0xFF7c4dff)
val endColor = Color(0xFF536dfe)

@Composable
fun RecursionLoop() {
    val state = rememberRecursionLoopState()
    val currentFrameTime = tickerMillis()
    state.StartLooping(frameTime = currentFrameTime)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startRadius = size.width / 2 * 0.42f
            var endRadius = size.width / 2 * 0.5f
            val interval = LoopConfiguration.DEFAULT_ITERATIONS_INTERVAL
            scale(state.shapeScaleFactor) {
                // invert the order of scale & translate and see another dope effect
                translate(left = center.x, top = center.y) {
                    for (i in -90 until state.iterationsNum step interval) {
                        // animate the scale of last drawn tick
                        drawTick(
                            angle = i,
                            startRadius = startRadius,
                            endRadius = endRadius,
                            brush = Brush.verticalGradient(
                                0.1f to startColor,
                                0.3f to midColor,
                                0.7f to endColor
                            ),
                            scaleConfig = {
                                // Enable scaling for the last drawn tick & for ticks that are 1+ cycle behind
                                val scaleDown = (i < state.iterationsNum - 360)
                                enabled = (i + interval >= state.iterationsNum) || scaleDown
                                scaleFactor = if (scaleDown) state.tickDownScaleFactor else state.tickUpScaleFactor
                            }
                        )
                        startRadius += startRadius * 0.01f
                        endRadius += endRadius * 0.01f
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawTick(
    angle: Int,
    startRadius: Float,
    endRadius: Float,
    brush: Brush,
    strokeWidth: Float = 12f,
    strokeCap: StrokeCap = StrokeCap.Round,
    scaleConfig: ScaleConfig.() -> Unit = {}
) {
    val theta = angle * PI / 180

    val startPos = Offset(
        x = (startRadius * cos(theta)).toFloat(),
        y = (startRadius * sin(theta)).toFloat()
    )

    val endPos = Offset(
        x = (endRadius * cos(theta)).toFloat(),
        y = (endRadius * sin(theta)).toFloat()
    )

    val pivotX = (startPos.x + endPos.x) / 2
    val pivotY = (startPos.y + endPos.y) / 2

    val config = ScaleConfig.apply { scaleConfig() }
    withScale(config.enabled, config.scaleFactor, Offset(pivotX, pivotY)) {
        // TODO: make another variant that is using PathEffect
        drawLine(
            brush = brush,
            start = startPos,
            end = endPos,
            strokeWidth = strokeWidth,
            cap = strokeCap
        )
    }
}

private object ScaleConfig {
    var enabled: Boolean = false

    var scaleFactor: Float = 1.0f
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun RecursionLoopPreview() {
    RecursionLoop()
}
