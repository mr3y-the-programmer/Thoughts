package com.mr3y.thoughts.components.foundation.draggable

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlin.math.roundToInt

@Composable
fun rememberDragState(@IntRange(from = 0) itemsNum: Int): DragState {
    return remember { DragState(itemsNum) }
}

@Immutable
internal data class Item(val itemIndex: Int, val translation: Int)

/**
 * manages [DraggableColumn], [DraggableRow] Items dragging state
 */
class DragState(private val itemsNum: Int) {
    var itemsCount by mutableStateOf(itemsNum)
        private set

    internal var itemSize by mutableStateOf(0)
    private var previousSteps by mutableStateOf(0)

    private val _lastDraggedItem: MutableState<Item?> = mutableStateOf(null)
    internal val lastDraggedItem: State<Item?>
        get() = _lastDraggedItem

    // items which need to be repositioned due to movement of dragged item
    private val _draggingAffectedItems = mutableStateListOf<Item>()
    internal val draggingAffectedItems: SnapshotStateList<Item>
        get() = _draggingAffectedItems

    fun drag(item: Int, to: Float) {
        // calculates how many steps should the dragged item be shifted.
        // For example, if it equals -1 so, item should shift place with the previous item
        // if it is 1, item should shift place with next item
        val steps = (((to + itemSize).roundToInt() / itemSize) - 1)
        val deltaSteps = steps - previousSteps
        _lastDraggedItem.value = Item(itemIndex = item, steps * itemSize)
        for (i in if (steps < 0) -1 downTo steps else 1..steps) {
            _draggingAffectedItems.plusAssign(
                Item(itemIndex = item + i, translation = (if (steps < 0) 1 else -1) * itemSize)
            )
        }
        previousSteps = steps
    }

    fun animateDrag(item: Int, to: Float) {
    }

    fun clearDragging() {
        _draggingAffectedItems.clear()
        _lastDraggedItem.value = null
    }
}
