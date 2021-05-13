package com.mr3y.thoughts.components.foundation.stackedpages

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Simulate Stacked pages effect
 */
@ExperimentalAnimationApi
@Composable
fun Stack() {
    val generator = remember { PagesGenerator() }
    val pages = remember { generator.pages }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        // generate new page each 1 second as long as our composable hasn't left the tree
        LaunchedEffect(true) {
            while (isActive) {
                launch {
                    delay(1000)
                    generator.addNewPage()
                }
            }
        }
        val pageModifier = { index: Float ->
            Modifier
                .fillMaxSize(0.5f)
                .zIndex(index)
                .graphicsLayer(scaleY = 1f + (index * 0.1f))
        }
        repeat(pages) { index ->
            if (index == pages - 1) {
                val rootWidthInPixel = with(LocalDensity.current) {
                    maxWidth.toPx()
                }
                val baseOffset = -(rootWidthInPixel / 8)
                val animatable = Animatable((index + 1) * baseOffset)
                LaunchedEffect(index) {
                    animatable.animateTo(baseOffset * index)
                }
                Page(
                    modifier = pageModifier(index.toFloat())
                        .offset(x = animatable.value.dp)
                )
            } else {
                Page(
                    modifier = pageModifier(index.toFloat())
                        .offset(x = -(maxWidth / 8) * index)
                )
            }
        }
    }
}

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

@ExperimentalAnimationApi
@Preview(widthDp = 360, heightDp = 640)
@Composable
fun StackPreview() {
    Stack()
}
