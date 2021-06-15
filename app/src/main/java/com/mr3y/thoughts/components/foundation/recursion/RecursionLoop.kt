package com.mr3y.thoughts.components.foundation.recursion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val startColor = Color(0xFFf06292)
val midColor = Color(0xFF7c4dff)
val endColor = Color(0xFF536dfe)
const val ITERATIONS_INTERVAL = 6

@Composable
fun RecursionLoop() {
    // TODO: refactor this into some state class
    var iterationsNum by remember { mutableStateOf(-90) }
    val newCycle by remember { derivedStateOf { iterationsNum % 270 == 0 } }
    val shapeScaleFactor = remember { Animatable(1.0f, Float.VectorConverter) }
    val tickScaleFactor = remember { Animatable(1.0f, Float.VectorConverter) }
    val tickScaleDownFactor = remember { Animatable(1.0f, Float.VectorConverter) }
    val currentFrameTime = tickerMillis()
    LaunchedEffect(currentFrameTime.value) {
        iterationsNum += ITERATIONS_INTERVAL
        /*if (newCycle) {
            launch {
                shapeScaleFactor.animateTo(shapeScaleFactor.value * (shapeScaleFactor.value / 2f))
            }
        }*/
        launch {
            shapeScaleFactor.animateTo(shapeScaleFactor.value - 0.00255f)
        }
        launch {
            tickScaleFactor.animateTo(
                2f,
                spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            tickScaleFactor.snapTo(1.0f) // reset to the default value
        }
        launch {
            tickScaleDownFactor.animateTo(
                0.5f,
                spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startRadius = size.width / 2 * 0.42f
            var endRadius = size.width / 2 * 0.5f
            scale(shapeScaleFactor.value) {
                // invert the order of scale & translate and see another dope effect
                translate(left = center.x, top = center.y) {
                    for (i in -90 until iterationsNum step ITERATIONS_INTERVAL) {
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
                            scalingEnabled = (i + ITERATIONS_INTERVAL >= iterationsNum) || (i < iterationsNum - 360),
                            scaleFactor = if (i < iterationsNum - 360) tickScaleDownFactor.value else tickScaleFactor.value
                        )
                        startRadius += startRadius * 0.01f
                        endRadius += endRadius * 0.01f
                    }
                }
            }
        }
    }
}

@Composable
fun tickerMillis(): State<Long> {
    val millis = remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        val startTime = withFrameMillis { it }
        while (true) {
            withFrameMillis { frameTime ->
                millis.value = frameTime - startTime
            }
        }
    }
    return millis
}

private fun DrawScope.withScale(
    enabled: Boolean,
    scaleFactor: Float,
    pivot: Offset,
    block: DrawScope.() -> Unit
) {
    if (!enabled) {
        block()
        return
    }
    scale(scaleFactor, pivot = pivot) {
        block()
    }
}

private fun DrawScope.drawTick(
    angle: Int,
    startRadius: Float,
    endRadius: Float,
    brush: Brush,
    scalingEnabled: Boolean,
    scaleFactor: Float = 1.2f,
    strokeWidth: Float = 12f,
    strokeCap: StrokeCap = StrokeCap.Round
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

    withScale(scalingEnabled, scaleFactor, Offset(pivotX, pivotY)) {
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

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun RecursionLoopPreview() {
    RecursionLoop()
}
