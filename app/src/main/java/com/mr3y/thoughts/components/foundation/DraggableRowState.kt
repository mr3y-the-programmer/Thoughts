package com.mr3y.thoughts.components.foundation

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
fun rememberDraggableRowState(@IntRange(from = 0) itemsNum: Int): DraggableRowState {
    return remember { DraggableRowState(itemsNum) }
}

@Immutable
internal data class RowItem(val itemIndex: Int, val translationX: Int)

/**
 * manages [DraggableColumn] Items dragging state
 */
class DraggableRowState(private val itemsNum: Int) {
    var itemsCount by mutableStateOf(itemsNum)
        private set

    internal var itemWidth by mutableStateOf(0)
    private var previousSteps by mutableStateOf(0)

    private val _lastDraggedItem: MutableState<RowItem?> = mutableStateOf(null)
    internal val lastDraggedItem: State<RowItem?>
        get() = _lastDraggedItem

    // items which need to be repositioned due to movement of dragged item
    private val _draggingAffectedItems = mutableStateListOf<RowItem>()
    internal val draggingAffectedItems: SnapshotStateList<RowItem>
        get() = _draggingAffectedItems

    fun drag(item: Int, to: Float) {
        // calculates how many steps should the dragged item be shifted.
        // For example, if it equals -1 so, item should shift place with the previous item
        // if it is 1, item should shift place with next item
        val steps = (((to + itemWidth).roundToInt() / itemWidth) - 1)
        val deltaSteps = steps - previousSteps
        _lastDraggedItem.value = RowItem(itemIndex = item, steps * itemWidth)
        for (i in if (steps < 0) -1 downTo steps else 1..steps) {
            _draggingAffectedItems.plusAssign(
                RowItem(itemIndex = item + i, translationX = (if (steps < 0) 1 else -1) * itemWidth)
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
