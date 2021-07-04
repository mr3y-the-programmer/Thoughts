package com.mr3y.thoughts.components.bottombar.state

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.GraphicsLayerScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberBottomBarState(numberOfTabs: Int, selectedTabIndex: Int, stateScope: CoroutineScope): BottomBarState {
    return remember { BottomBarState(numberOfTabs, selectedTabIndex, stateScope) }
}

class BottomBarState @VisibleForTesting internal constructor(
    numberOfTabs: Int,
    selectedTab: Int,
    private val animationScope: CoroutineScope
) {

    internal var tabsNumExcludingCurve by mutableStateOf(numberOfTabs - 1)
        private set

    internal var selectedTabIndex by mutableStateOf(selectedTab)
        private set

    init {
        require(numberOfTabs > 2 && selectedTab >= 0) {
            "Invalid number of tabs: $numberOfTabs, or SelectedTabIndex: $selectedTab argument"
        }
    }

    @VisibleForTesting
    internal var currentCurveTranslationX by mutableStateOf(0)
        private set

    private val animatedTranslationX = Animatable(0, Int.VectorConverter)

    fun animateCurveToPosition(index: Int, animationSpec: AnimationSpec<Int>) {
        if (index == selectedTabIndex) return
        val newPosition = (bottomBarLayoutMetadata.tabWidth * index)
        selectedTabIndex = index
        animationScope.launch {
            animatedTranslationX.animateTo(newPosition, animationSpec) {
                currentCurveTranslationX = this.value
            }
        }
    }

    internal fun curveGraphicsLayer(scope: GraphicsLayerScope) {
        scope.translationX = if (currentCurveTranslationX != 0) {
            currentCurveTranslationX
        } else {
            bottomBarLayoutMetadata.tabWidth * selectedTabIndex
        }.toFloat()
    }
}
