package com.mr3y.thoughts.components.bottombar

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

/**
 * A Custom Layout to position BottomBar tabs & curve horizontally like [Row] layout.
 * The difference is [Row] positions items in the order they are declared, this layout allows for more flexibility
 * by controlling the position of its elements while taking advantage of some [Row] layout properties.
 */
@Composable
internal fun BottomBarLayout(
    modifier: Modifier = Modifier,
    curve: @Composable BottomBarScope.() -> Unit,
    content: @Composable BottomBarScope.() -> Unit,
) {
}

@LayoutScopeMarker
@Immutable
interface BottomBarScope {

    @Stable
    fun Modifier.weight(weight: Float): Modifier

    @Stable
    fun Modifier.order(order: Float): Modifier
}
