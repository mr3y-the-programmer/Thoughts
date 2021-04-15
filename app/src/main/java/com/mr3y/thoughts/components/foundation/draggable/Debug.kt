package com.mr3y.thoughts.components.foundation.draggable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(heightDp = 300, widthDp = 200)
@Composable
fun DebugDragPreview() {
    var deltaY by remember { mutableStateOf(0f) }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                // NOTE: layout/offset or any other layouting modifier must take precedence
                // before pointerInput modifier or forEachGesture will be meaningless
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        placeable.placeRelative(
                            0,
                            deltaY
                                .coerceIn(0f, (maxHeight.toPx() - 40.dp.toPx()))
                                .toInt()
                        )
                        Log.d("DraggableItem", "currentHeight: ${placeable.height}")
                        Log.d("DraggableItem", "height + drag: ${placeable.height + deltaY}")
                    }
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = { /*Do some cleanup if you want*/ },
                        onDragCancel = { deltaY = 0f }
                    ) { change, dragAmount ->
                        deltaY += dragAmount
                        change.consumeAllChanges()
                    }
                    /*forEachGesture {
                        val touchGestureId = awaitPointerEventScope { awaitFirstDown().id }
                        // track this gesture dragging
                        awaitPointerEventScope {
                            verticalDrag(touchGestureId) { change ->
                                deltaY += change.positionChange().y
                                change.consumeAllChanges()
                            }
                        }
                    }*/
                }
        ) {
            Text(
                text = "Drag me around, CurrentValue: $deltaY",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
