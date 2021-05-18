package com.mr3y.thoughts.components.foundation.stackedpages

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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Simulate Stacked pages effect
 */
@Composable
fun Stack() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        val pagesNum = remember { mutableStateOf(2) }
        val currentFrameTimeInMillis = tickerMillis()
        val baseOffset = (-maxWidth / 16)
        val startVal = baseOffset * (pagesNum.value - 1)
        val endVal = baseOffset * pagesNum.value
        var lastPageXCoordinate by remember { mutableStateOf(startVal) }
        lastPageXCoordinate += lerp(startVal, endVal, 0.2f.dp, currentFrameTimeInMillis.value)

        // generate new page each 1 second as long as our composable hasn't left the tree
        LaunchedEffect(true) {
            while (isActive && pagesNum.value < 9) {
                delay(1000)
                pagesNum.value++
                // reset the x coordinate of last page to animate the new last page
                lastPageXCoordinate = 0.dp
            }
        }

        val pages = buildPages(pagesNum.value) { index ->
            val normalOffset = baseOffset * 2 * index
            if (index == pagesNum.value - 1) Page(lastPageXCoordinate) else Page(normalOffset)
        }

        pages.forEachIndexed { index, page ->
            Page(
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .zIndex(index.toFloat())
                    .graphicsLayer(scaleY = 1f + (index * 0.1f))
                    .offset(x = page.offsetX)
            )
        }
    }
}

private fun buildPages(count: Int, factory: (index: Int) -> Page): List<Page> {
    val pages = mutableListOf<Page>()
    repeat(count) { index ->
        pages.add(factory(index))
    }
    return pages
}

@Composable
private fun lerp(
    startValue: Dp,
    endValue: Dp,
    fraction: Dp,
    value: Long
): Dp {
    val (endValInPx, startValInPx) = with(LocalDensity.current) {
        endValue.toPx() to startValue.toPx()
    }
    return fraction * (endValInPx - startValInPx)
}

data class Page(val offsetX: Dp)

@Composable
fun Page(modifier: Modifier = Modifier) {
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

@Composable
fun tickerMillis(): State<Long> {
    val millis = remember { mutableStateOf(0L) }
    LaunchedEffect(key1 = Unit) {
        while (true) {
            withFrameMillis {
                millis.value = it
            }
        }
    }
    return millis
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun StackPreview() {
    Stack()
}
