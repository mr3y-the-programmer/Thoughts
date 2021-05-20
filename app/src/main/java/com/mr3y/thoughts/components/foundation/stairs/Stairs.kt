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
import androidx.compose.runtime.mutableStateOf
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
        val stepsNum = remember { mutableStateOf(1) }

        // generate new step each 1 second as long as our composable hasn't left the tree
        LaunchedEffect(true) {
            while (isActive && stepsNum.value < 9) {
                delay(1000)
                stepsNum.value++
            }
        }

        val steps = buildSteps(stepsNum.value) { index ->
            val baseOffset = (-maxWidth / 16)
            val normalOffset = baseOffset * 2 * index
            if (index == stepsNum.value - 1) Step(normalOffset) else Step(normalOffset)
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

private fun buildSteps(count: Int, factory: (index: Int) -> Step): List<Step> {
    val steps = mutableListOf<Step>()
    repeat(count) { index ->
        steps.add(factory(index))
    }
    return steps
}

data class Step(val offsetX: Dp)

@Composable
fun StairsStep(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .background(Color.DarkGray.copy(alpha = 0.15f))
            .padding(end = 4.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 4.dp,
        border = BorderStroke(2.dp, Color.Black),
        content = {}
    )
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun StairsPreview() {
    Stairs()
}
