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
    corners: CornerBasedShape
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
        val circleRadius = halfWidth * 1 / 3
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
                    Rect(0f,0f, -bottomEnd, bottomEnd),
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
    ): CornerBasedShape {
        TODO("Not yet implemented")
    }
}
