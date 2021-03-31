package com.mr3y.thoughts.components.foundation

import android.util.Log
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// holds the index of each item in the row
@Immutable
private inline class RowItemIndex(val index: Int) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@RowItemIndex
}

private val Measurable.index: Int
    get() = (parentData as RowItemIndex).index

@Composable
fun DraggableRow(
    state: DraggableRowState,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    itemSpacing: Dp = 0.dp,
    content: @Composable DraggableRowScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    var currentSessionDragX by remember { mutableStateOf(0f) }
    val draggedItem by remember { state.lastDraggedItem }
    val affectedItems = remember { state.draggingAffectedItems }
    Layout(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(state = scrollState),
        content = {
            repeat(state.itemsCount) { itemIndex ->
                key(itemIndex) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        state.clearDragging()
                                        currentSessionDragX = 0f
                                    },
                                    onDragCancel = { state.clearDragging() },
                                    onDragStart = { }
                                ) { change, dragDelta ->
                                    change.consumeAllChanges()
                                    currentSessionDragX += dragDelta
                                    state.drag(item = itemIndex, to = currentSessionDragX)
                                    // state.animateDrag(item = itemIndex, to = drag)
                                }
                            }
                            .then(RowItemIndex(itemIndex))
                    ) {
                        val scope = DraggableRowScopeImpl(itemIndex)
                        scope.content()
                    }
                }
            }
        }
    ) { measurables, constraints ->
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map {
            it.index to it.measure(childConstraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { indexedPlaceable ->
                val index = indexedPlaceable.first
                val placeable = indexedPlaceable.second
                state.itemWidth = (placeable.width + itemSpacing.roundToPx())
                val offsetX = ((placeable.width + itemSpacing.roundToPx()) * index)
                val translationX = when (index) {
                    draggedItem?.itemIndex -> draggedItem?.translationX ?: 0
                    in affectedItems.map { it.itemIndex } -> affectedItems.first { it.itemIndex == index }.translationX
                    else -> 0
                }
                val offsetY = verticalAlignment.align(
                    size = placeable.height,
                    space = constraints.maxHeight
                )
                placeable.placeWithLayer(
                    x = offsetX,
                    y = offsetY,
                    layerBlock = {
                        this.translationX = translationX.toFloat()
                    }
                )
                Log.v("DraggableRow", "Positioning item: $index at $offsetX")
            }
        }
    }
}

interface DraggableRowScope {

    val currentItemIndex: Int
}

internal class DraggableRowScopeImpl(private val index: Int) : DraggableRowScope {
    override val currentItemIndex: Int
        get() = index
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun DraggableRowDemo() {
    val state = rememberDraggableRowState(itemsNum = 15)
    DraggableRow(state = state, itemSpacing = 20.dp) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(96.dp, 48.dp)) {
            Text(text = "Button $currentItemIndex")
        }
    }
}
