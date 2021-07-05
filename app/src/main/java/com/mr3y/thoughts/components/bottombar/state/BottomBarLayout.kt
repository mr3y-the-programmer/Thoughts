package com.mr3y.thoughts.components.bottombar.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeLayoutState
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

internal enum class BottomBarSlots {
    Tab,

    Curve,

    Fab
}

/**
 * A Custom Layout to position BottomBar tabs & curve horizontally like [Row] layout.
 * The difference is [Row] positions items in the order they are declared, this layout allows for more flexibility
 * by controlling the position of its elements while taking advantage of some [Row] layout properties.
 */
@Composable
internal fun BottomBarLayout(
    state: BottomBarState,
    modifier: Modifier = Modifier,
    bottomBarItemsHeight: Dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    fab: @Composable BottomBarScope.(selectedIndex: Int) -> Unit,
    curve: @Composable BottomBarScope.() -> Unit,
    tab: @Composable BottomBarScope.(index: Int) -> Unit,
) {
    val bottomBarScope = BottomBarScopeImpl
    val subcomposeState = remember { SubcomposeLayoutState(1) }
    SubcomposeLayout(state = subcomposeState, modifier = modifier) { parentConstraints ->
        val itemsHeight = bottomBarItemsHeight.roundToPx()
        val curveMeasurable = subcompose(BottomBarSlots.Curve) {
            bottomBarScope.curve()
        }.single()

        val tabMeasureables = subcompose(BottomBarSlots.Tab) {
            repeat(state.tabsNumExcludingCurve) { index ->
                val tabIndex = index.mapToCorrespondingIndex(state.selectedTabIndex)
                bottomBarScope.tab(tabIndex)
            }
        }

        val fabMeasurable = subcompose(BottomBarSlots.Fab) {
            bottomBarScope.fab(state.selectedTabIndex)
        }.single()

        // note that all tabs will be weighted equally
        val tabsWeight = tabMeasureables.map { it.weight }.sum()
        val (curveWidth, tabsWidth) = parentConstraints.maxWidth.partitionByRatio(
            Ratio(curveMeasurable.weight, tabsWeight)
        )
        val tabWidth = (tabsWidth / state.tabsNumExcludingCurve).roundToInt()
        val curveConstraints = parentConstraints
            .copy(minWidth = 0, maxWidth = curveWidth.roundToInt(), minHeight = 0, maxHeight = itemsHeight)

        val curvePlaceable = curveMeasurable.measure(curveConstraints)

        val tabConstraints = parentConstraints.copy(minWidth = 0, maxWidth = tabWidth, minHeight = 0, maxHeight = itemsHeight)
        val tabPlaceables = tabMeasureables.map { it.measure(tabConstraints) }

        val fabHeight = (fabMeasurable.weight.coerceIn(0.1f..1f) * parentConstraints.maxHeight)
        val fabConstraints = parentConstraints.copy(minWidth = 0, maxWidth = curvePlaceable.measuredWidth, minHeight = 0, maxHeight = fabHeight.roundToInt())
        val fabPlaceable = fabMeasurable.measure(fabConstraints)
        fillLayoutMetadata {
            this.tabWidth = tabWidth
            this.curveWidth = curvePlaceable.measuredWidth
            this.fabWidth = fabPlaceable.measuredWidth
            this.fabHeight = fabPlaceable.measuredHeight
        }
        layout(parentConstraints.maxWidth, parentConstraints.maxHeight) {
            var x = 0
            val itemsOffsetY = parentConstraints.maxHeight - itemsHeight
            tabPlaceables.forEachIndexed { index, tab ->
                val tabAlignment = verticalAlignment.align(tab.measuredHeight, itemsHeight)
                tab.place(x, itemsOffsetY + tabAlignment)
                x += when {
                    index == state.selectedTabIndex - 1 -> (tabWidth + curveConstraints.maxWidth)
                    else -> tabWidth
                }
            }
            // Layout the curve
            val curveAlignment = verticalAlignment.align(curvePlaceable.measuredHeight, itemsHeight)
            curvePlaceable.placeWithLayer(0, itemsOffsetY + curveAlignment) {
                state.curveGraphicsLayer(this)
            }
            // Layout the fab
            val fabHorizontalAlignment = Alignment.CenterHorizontally
                .align(fabPlaceable.measuredWidth, curvePlaceable.measuredWidth, layoutDirection)
            fabPlaceable.placeWithLayer(fabHorizontalAlignment, 0) {
                state.curveGraphicsLayer(this)
            }
        }
    }
}
