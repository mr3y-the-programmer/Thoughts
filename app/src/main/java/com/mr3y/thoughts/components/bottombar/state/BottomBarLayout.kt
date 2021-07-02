package com.mr3y.thoughts.components.bottombar.state

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeLayoutState
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
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    fab: @Composable BottomBarScope.() -> Unit,
    curve: @Composable BottomBarScope.() -> Unit,
    curveAnimationSpec: AnimationSpec<Float>? = spring(Spring.DampingRatioLowBouncy),
    tab: @Composable BottomBarScope.(index: Int) -> Unit,
) {
    val bottomBarScope = BottomBarScopeImpl
    val subcomposeState = remember { SubcomposeLayoutState(1) }
    SubcomposeLayout(state = subcomposeState, modifier = modifier) { parentConstraints ->
        val curveMeasurable = subcompose(BottomBarSlots.Curve) {
            bottomBarScope.curve()
        }.first()

        val tabMeasureables = subcompose(BottomBarSlots.Tab) {
            repeat(state.tabsNumExcludingCurve) { tabIndex ->
                bottomBarScope.tab(tabIndex)
            }
        }
        // note that all tabs will be weighted equally
        val tabsWeight = tabMeasureables.map { it.weight }.sum()
        val (curveWidth, tabsWidth) = parentConstraints.maxWidth.partitionByRatio(Ratio(curveMeasurable.weight, tabsWeight))
        val tabWidth = (tabsWidth / state.tabsNumExcludingCurve).roundToInt()
        val curveConstraints = parentConstraints
            .copy(minWidth = 0, maxWidth = curveWidth.roundToInt())

        val curvePlaceable = curveMeasurable.measure(curveConstraints)

        val tabConstraints = parentConstraints.copy(minWidth = 0, maxWidth = tabWidth)
        val tabPlaceables = tabMeasureables.map { it.measure(tabConstraints) }
        layout(parentConstraints.maxWidth, parentConstraints.maxHeight) {
            var x = 0
            tabPlaceables.forEachIndexed { index, tab ->
                val tabAlignment = verticalAlignment.align(tab.height, parentConstraints.maxHeight)
                tab.place(x, tabAlignment)
                x += when {
                    index == state.selectedTabIndex - 1 -> (tabWidth + curveConstraints.maxWidth)
                    else -> tabWidth
                }
            }
            // Layout the curve
            val curveAlignment = verticalAlignment.align(curvePlaceable.height, parentConstraints.maxHeight)
            if (curveAnimationSpec != null) {
                state.translateToNewXPosition((tabWidth * state.selectedTabIndex).toFloat(), curveAnimationSpec)
                curvePlaceable.placeWithLayer((tabWidth * state.selectedTabIndex), 0) {
                    translationX = state.curveTranslationX
                }
            } else {
                curvePlaceable.place((tabWidth * state.selectedTabIndex), curveAlignment)
            }
        }
    }
}
