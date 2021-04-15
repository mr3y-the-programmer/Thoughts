package com.mr3y.thoughts.components.foundation.draggable

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DraggableRow(
    state: DragState,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    itemSpacing: Dp = 0.dp,
    content: @Composable DraggableLayoutScope.() -> Unit
) {
    DraggableLayout(
        state = state,
        isVertical = false,
        horizontalAlignment = null,
        verticalAlignment = verticalAlignment,
        itemSpacing = itemSpacing,
        modifier = modifier,
        content = content
    )
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun DraggableRowDemo() {
    val state = rememberDragState(itemsNum = 15)
    DraggableRow(state = state, itemSpacing = 20.dp) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(96.dp, 48.dp)) {
            Text(text = "Button $currentItemIndex")
        }
    }
}
