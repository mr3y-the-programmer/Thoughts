package com.mr3y.thoughts.components.bottombar.state

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberBottomBarState(numberOfTabs: Int, selectedTabIndex: Int): BottomBarState {
    return remember { BottomBarState(numberOfTabs, selectedTabIndex) }
}

class BottomBarState @VisibleForTesting internal constructor(numberOfTabs: Int, selectedTab: Int) {

    internal var tabsNumExcludingCurve by mutableStateOf(numberOfTabs - 1)
        private set

    internal var selectedTabIndex by mutableStateOf(selectedTab)
        private set

    init {
        require(numberOfTabs > 2 && selectedTab >= 0) {
            "Invalid number of tabs: $numberOfTabs, or SelectedTabIndex: $selectedTab argument"
        }
    }

    fun updateSelectedIndex(newIndex: Int) {
        selectedTabIndex = newIndex
    }
}
