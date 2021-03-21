package com.mr3y.thoughts.components.foundation.stackedpages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

/**
 * Simulate Stacked pages effect
 */
@ExperimentalAnimationApi
@Composable
fun Stack() {
    val generator = remember { PagesGenerator() }
    val pages = remember { generator.pages }
    var visible by remember { mutableStateOf(false) }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        // generate new page each 1 second as long as our composable hasn't left the tree
        LaunchedEffect(true) {
            delay(1000)
            generator.addNewPage()
            visible = !visible
        }
        repeat(pages) { index ->
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally({ -(it / 8) * index }),
                exit = slideOutHorizontally({ -(it / 8) * index })
            ) {
                Page(
                    modifier = Modifier
                        .fillMaxSize(0.5f)
                        .offset(x = -(maxWidth / 8) * index)
                        .zIndex(index.toFloat())
                )
            }
        }
    }
}

@Composable
fun Page(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxSize(),
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
