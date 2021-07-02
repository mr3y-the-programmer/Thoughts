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

    private var previousCurveTranslationX by mutableStateOf(0f)

    internal var curveTranslationX by mutableStateOf(0f)
        private set

    private val animatedTranslationX = Animatable(previousCurveTranslationX, Float.VectorConverter)

    fun translateToNewXPosition(newPosition: Float, animationSpec: AnimationSpec<Float>) {
        val pos = newPosition - previousCurveTranslationX
        if (pos == newPosition) {
            previousCurveTranslationX = newPosition
            return
        }
        animationScope.launch {
            animatedTranslationX.animateTo(pos, animationSpec) {
                curveTranslationX = this.value
            }
            previousCurveTranslationX = curveTranslationX
        }
    }
}
