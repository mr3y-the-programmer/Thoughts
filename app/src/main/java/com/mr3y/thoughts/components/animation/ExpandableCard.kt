package com.mr3y.thoughts.components.animation

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A card which expand smoothly to fill the full-screen when clicked
 */
@Composable
fun ExpandableCard(
    modifier: Modifier,
    expandedSize: Size,
    art: Painter,
    brief: String,
    onExpand: @Composable () -> Unit
) {
    // TODO: convert to rememberSaveable
    var expanded by remember { mutableStateOf(false) }
    var size by remember { mutableStateOf(Size.Zero) }
    Card(
        modifier = modifier
            .clickable { expanded = !expanded }
            .onGloballyPositioned { size = it.size },
        elevation = 12.dp
    ) {
        val cardExpandTransition = if (!expanded) {
            updateTransition(targetState = size)
        } else {
            updateTransition(targetState = expandedSize)
        }
        val imageScale: Float by cardExpandTransition.animateFloat(transitionSpec = { tween(200) }) {
            if (it == expandedSize) 2f else 1f
        }
        val textScale: Float by cardExpandTransition.animateFloat(transitionSpec = { tween(200) }) {
            if (it == expandedSize) 1.5f else 1f
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = art,
                modifier = Modifier
                    .size((100 * imageScale).dp)
                    .weight(2f),
                contentDescription = "Preview"
            )
            Text(
                text = brief,
                modifier = Modifier
                    .size((100 * textScale).dp)
                    .weight(1f),
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
