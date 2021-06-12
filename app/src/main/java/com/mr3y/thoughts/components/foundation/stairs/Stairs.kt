package com.mr3y.thoughts.components.foundation.stairs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Simulate Stairs effect
 */
@Composable
fun Stairs() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .rotate(90f),
        contentAlignment = Alignment.Center
    ) {
        val stepOffset = { index: Int ->
            val baseOffset = (-maxWidth / 16)
            baseOffset * 2 * index
        }
        // initialize state list with 1 step by default
        val steps = remember { mutableStateListOf(Step(stepOffset(0))) }

        // generate new step each 1 second as long as our composable hasn't left the tree
        LaunchedEffect(true) {
            while (isActive && steps.size < 9) {
                delay(1000)
                steps.add(Step(stepOffset(steps.indexOfLast { true } + 1)))
            }
        }

        steps.forEachIndexed { index, step ->
            StairsStep(
                modifier = Modifier
                    .fillMaxSize(0.35f)
                    .zIndex(index.toFloat())
                    .graphicsLayer(scaleY = 1f + (index * 0.1f))
                    .offset(x = step.offsetX)
            )
        }
    }
}

data class Step(val offsetX: Dp)

@Composable
fun StairsStep(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray.copy(alpha = 0.25f), shape = RoundedCornerShape(32.dp))
            .padding(end = 8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 104.dp,
        border = BorderStroke(2.dp, Color.Black),
        content = {}
    )
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun StairsPreview() {
    Stairs()
}
