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
    selectedTabIndex: Int,
    private val animationScope: CoroutineScope
) {

    internal var tabsNumExcludingCurve by mutableStateOf(numberOfTabs - 1)
        private set

    internal var selectedTabIndex by mutableStateOf(selectedTabIndex)
        private set

    init {
        require(numberOfTabs > 2 && selectedTabIndex >= 0) {
            "Invalid number of tabs: $numberOfTabs, or SelectedTabIndex: $selectedTabIndex argument"
        }
    }

    private var previousCurveTranslationX by mutableStateOf(0)

    @VisibleForTesting
    internal var currentCurveTranslationX by mutableStateOf(0)
        private set

    private val animatedTranslationX = Animatable(previousCurveTranslationX, Int.VectorConverter)

    fun animateCurveToPosition(index: Int, animationSpec: AnimationSpec<Int>) {
        if (index == selectedTabIndex) return
        val newPosition = (bottomBarLayoutMetadata.tabWidth * index)
        selectedTabIndex = index
        val pos = newPosition - previousCurveTranslationX
        animationScope.launch {
            animatedTranslationX.animateTo(pos, animationSpec) {
                currentCurveTranslationX = this.value
            }
            previousCurveTranslationX = currentCurveTranslationX
        }
    }

    internal fun curveGraphicsLayer(scope: GraphicsLayerScope) {
        scope.translationX = currentCurveTranslationX.toFloat()
    }
}
