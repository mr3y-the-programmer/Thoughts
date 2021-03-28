package com.mr3y.thoughts.components.foundation

import android.util.Log
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

// holds the index of each item in the column
@Immutable
private inline class ColumnItemIndex(val index: Int) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@ColumnItemIndex
}

private val Measurable.index: Int
    get() = (parentData as ColumnItemIndex).index

@Composable
fun DraggableColumn(
    state: DraggableColumnState,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    itemSpacing: Dp = 0.dp,
    content: @Composable DraggableColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    var currentSessionDragY by remember { mutableStateOf(0f) }
    val draggedItem by remember { state.lastDraggedColumnItem }
    val affectedItems = remember { state.draggingAffectedColumnItems }
    Layout(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        content = {
            repeat(state.itemsCount) { itemIndex ->
                key(itemIndex) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectVerticalDragGestures(
                                    onDragEnd = {
                                        state.clearDragging()
                                        currentSessionDragY = 0f
                                    },
                                    onDragCancel = { state.clearDragging() },
                                    onDragStart = { }
                                ) { change, dragDelta ->
                                    change.consumeAllChanges()
                                    currentSessionDragY += dragDelta
                                    state.drag(item = itemIndex, to = currentSessionDragY)
                                    // state.animateDrag(item = itemIndex, to = drag)
                                }
                            }
                            .then(ColumnItemIndex(itemIndex))
                    ) {
                        val scope = DraggableColumnScopeImpl(itemIndex)
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
                state.itemHeight = (placeable.height + itemSpacing.roundToPx())
                val offsetY = ((placeable.height + itemSpacing.roundToPx()) * index)
                val translationY = when (index) {
                    draggedItem?.itemIndex -> draggedItem?.translationY ?: 0
                    in affectedItems.map { it.itemIndex } -> affectedItems.first { it.itemIndex == index }.translationY
                    else -> 0
                }
                val offsetX = horizontalAlignment.align(
                    size = placeable.width,
                    space = constraints.maxWidth,
                    layoutDirection = LayoutDirection.Ltr
                )
                placeable.placeWithLayer(
                    x = offsetX,
                    y = offsetY,
                    layerBlock = {
                        this.translationY = translationY.toFloat()
                    }
                )
                Log.v("DraggableColumn", "Positioning item: $index at $offsetY")
            }
        }
    }
}

interface DraggableColumnScope {

    val currentItemIndex: Int
}

internal class DraggableColumnScopeImpl(private val index: Int) : DraggableColumnScope {
    override val currentItemIndex: Int
        get() = index
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun DraggableColumnDemo() {
    val state = rememberDraggableColumnState(itemsNum = 15)
    DraggableColumn(state = state, itemSpacing = 20.dp) {
        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(96.dp, 48.dp)) {
            Text(text = "Button $currentItemIndex")
        }
    }
}
