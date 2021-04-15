package com.mr3y.thoughts.components.foundation.draggable

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

// holds the index of each item in the layout
@Immutable
private inline class Index(val index: Int) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@Index
}

private val Measurable.index: Int
    get() = (parentData as Index).index

@Composable
internal fun DraggableLayout(
    state: DragState,
    isVertical: Boolean,
    horizontalAlignment: Alignment.Horizontal?,
    verticalAlignment: Alignment.Vertical?,
    itemSpacing: Dp,
    modifier: Modifier = Modifier,
    content: @Composable DraggableLayoutScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    var currentSessionDrag by remember { mutableStateOf(0f) }
    val draggedItem by remember { state.lastDraggedItem }
    val affectedItems = remember { state.draggingAffectedItems }
    Layout(
        modifier = modifier
            .fillMaxSize()
            .scrollable(isVertical, scrollState),
        content = {
            repeat(state.itemsCount) { itemIndex ->
                key(itemIndex) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                if (isVertical) {
                                    detectVerticalDragGestures(
                                        onDragEnd = {
                                            state.clearDragging()
                                            currentSessionDrag = 0f
                                        },
                                        onDragCancel = { state.clearDragging() },
                                        onDragStart = { }
                                    ) { change, dragDelta ->
                                        change.consumeAllChanges()
                                        currentSessionDrag += dragDelta
                                        state.drag(item = itemIndex, to = currentSessionDrag)
                                        // state.animateDrag(item = itemIndex, to = drag)
                                    }
                                } else {
                                    detectHorizontalDragGestures(
                                        onDragEnd = {
                                            state.clearDragging()
                                            currentSessionDrag = 0f
                                        },
                                        onDragCancel = { state.clearDragging() },
                                        onDragStart = { }
                                    ) { change, dragDelta ->
                                        change.consumeAllChanges()
                                        currentSessionDrag += dragDelta
                                        state.drag(item = itemIndex, to = currentSessionDrag)
                                        // state.animateDrag(item = itemIndex, to = drag)
                                    }
                                }
                            }
                            .then(Index(itemIndex))
                    ) {
                        val scope = DraggableLayoutScopeImpl(itemIndex)
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
                state.itemSize = ((if (isVertical) placeable.height else placeable.width) + itemSpacing.roundToPx())
                val offset = (((if (isVertical) placeable.height else placeable.width) + itemSpacing.roundToPx()) * index)
                val translation = when (index) {
                    draggedItem?.itemIndex -> draggedItem?.translation ?: 0
                    in affectedItems.map { it.itemIndex } -> affectedItems.first { it.itemIndex == index }.translation
                    else -> 0
                }
                val xAlignment = horizontalAlignment?.align(
                    size = placeable.width,
                    space = constraints.maxWidth,
                    layoutDirection = LayoutDirection.Ltr
                ) ?: 0
                val yAlignment = verticalAlignment?.align(
                    size = placeable.height,
                    space = constraints.maxHeight
                ) ?: 0
                if (isVertical) {
                    placeable.placeWithLayer(
                        x = xAlignment,
                        y = offset,
                        layerBlock = {
                            this.translationY = translation.toFloat()
                        }
                    )
                } else {
                    placeable.placeWithLayer(
                        x = offset,
                        y = yAlignment,
                        layerBlock = {
                            this.translationX = translation.toFloat()
                        }
                    )
                }

                Log.v("DraggableLayout", "Positioning item: $index at $offset")
            }
        }
    }
}

// Uncomment once you've updated to kotlin 1.5
/*sealed*/ interface DraggableLayoutScope {
    val currentItemIndex: Int
}

private class DraggableLayoutScopeImpl(private val index: Int) : DraggableLayoutScope {
    override val currentItemIndex: Int
        get() = index
}

private fun Modifier.scrollable(isVertical: Boolean, state: ScrollState): Modifier = composed {
    return@composed if (isVertical) verticalScroll(state) else horizontalScroll(state)
}
