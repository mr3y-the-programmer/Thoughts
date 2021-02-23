package com.mr3y.thoughts.components.bottombar

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.LayoutDirection

/**
 * A CornerBasedShape which clips a notch/half circle in the middle of the bottom bar.
 * also it draws the corners shape of bottom bar but currently it supports only single type for
 * all corners, in the future it maybe modified to allow each corner to use a different shape
 */
class NotchShape(
    corners: CornerBasedShape,
    private val notchRadius: Float
) : CornerBasedShape(corners.topStart, corners.topEnd, corners.bottomEnd, corners.bottomStart) {

    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection
    ): Outline {
        val width = size.width
        val halfWidth = width / 2
        val circleRadius = notchRadius.coerceIn(halfWidth * 1 / 4, halfWidth) // quick check
        return Outline.Generic(
            Path().apply {
                // moveTo(0f, topStart)
                if (layoutDirection == LayoutDirection.Ltr) arcTo(
                    Rect(0f, 0f, right = topStart, bottom = topStart),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                ) else arcTo(
                    Rect(left = topStart, 0f, 0f, bottom = topStart),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(halfWidth - topStart - circleRadius, 0f)
                if (layoutDirection == LayoutDirection.Ltr) arcTo(
                    Rect(0f, 0f, circleRadius * 2, circleRadius),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                ) else arcTo(
                    Rect(circleRadius * 2, 0f, 0f, circleRadius),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                lineTo((halfWidth - circleRadius) - topEnd, 0f)
                if (layoutDirection == LayoutDirection.Ltr) arcTo(
                    Rect(0f, 0f, right = topEnd, bottom = topEnd),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                ) else arcTo(
                    Rect(topEnd, 0f, 0f, topEnd),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(0f, (size.height - topEnd) - bottomEnd)
                if (layoutDirection == LayoutDirection.Ltr) arcTo(
                    Rect(-bottomEnd, 0f, 0f, bottomEnd),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                ) else arcTo(
                    Rect(0f, 0f, -bottomEnd, bottomEnd),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(-(width - bottomEnd) + bottomStart, 0f)
                if (layoutDirection == LayoutDirection.Ltr) arcTo(
                    Rect(-bottomStart, -bottomStart, 0f, 0f),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                ) else arcTo(
                    Rect(0f, top = -bottomStart, right = -bottomStart, 0f),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                close()
            }
        )
    }

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ): CornerBasedShape = throw NotImplementedError("copy() fun isn't supported yet.")

    // TODO: replace those functions with a compiler plugin to avoid boilerplate
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NotchShape) return false

        if (topStart != other.topStart) return false
        if (topEnd != other.topEnd) return false
        if (bottomEnd != other.bottomEnd) return false
        if (bottomStart != other.bottomStart) return false
        if (notchRadius != other.notchRadius) return false

        return true
    }

    override fun hashCode(): Int {
        var result = topStart.hashCode()
        result = 31 * result + topEnd.hashCode()
        result = 31 * result + bottomEnd.hashCode()
        result = 31 * result + bottomStart.hashCode()
        result = 31 * result + notchRadius.hashCode()
        return result
    }

    override fun toString(): String {
        return "NotchShape(topStart = $topStart, topEnd = $topEnd, notchRadius = $notchRadius," +
            " bottomEnd = $bottomEnd, bottomStart = $bottomStart)"
    }
}
