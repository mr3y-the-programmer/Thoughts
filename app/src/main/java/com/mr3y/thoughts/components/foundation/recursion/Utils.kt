package com.mr3y.thoughts.components.foundation.recursion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale

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

internal fun DrawScope.withScale(
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
