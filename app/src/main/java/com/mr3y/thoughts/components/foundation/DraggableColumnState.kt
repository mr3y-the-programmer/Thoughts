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
fun rememberDraggableColumnState(@IntRange(from = 0) itemsNum: Int): DraggableColumnState {
    return remember { DraggableColumnState(itemsNum) }
}

@Immutable
internal data class ColumnItem(val itemIndex: Int, val translationY: Int)

/**
 * manages [DraggableColumn] Items dragging state
 */
class DraggableColumnState(private val itemsNum: Int) {
    var itemsCount by mutableStateOf(itemsNum)
        private set

    internal var itemHeight by mutableStateOf(0)
    private var previousSteps by mutableStateOf(0)

    private val _lastDraggedColumnItem: MutableState<ColumnItem?> = mutableStateOf(null)
    internal val lastDraggedColumnItem: State<ColumnItem?>
        get() = _lastDraggedColumnItem

    // items which need to be repositioned due to movement of dragged item
    private val _draggingAffectedItems = mutableStateListOf<ColumnItem>()
    internal val draggingAffectedColumnItems: SnapshotStateList<ColumnItem>
        get() = _draggingAffectedItems

    fun drag(item: Int, to: Float) {
        // calculates how many steps should the dragged item be shifted.
        // For example, if it equals -1 so, item should shift place with the previous item
        // if it is 1, item should shift place with next item
        val steps = (((to + itemHeight).roundToInt() / itemHeight) - 1)
        val deltaSteps = steps - previousSteps
        _lastDraggedColumnItem.value = ColumnItem(itemIndex = item, steps * itemHeight)
        for (i in if (steps < 0) -1 downTo steps else 1..steps) {
            _draggingAffectedItems.plusAssign(
                ColumnItem(itemIndex = item + i, translationY = (if (steps < 0) 1 else -1) * itemHeight)
            )
        }
        previousSteps = steps
    }

    fun animateDrag(item: Int, to: Float) {
    }

    fun clearDragging() {
        _draggingAffectedItems.clear()
        _lastDraggedColumnItem.value = null
    }
}
