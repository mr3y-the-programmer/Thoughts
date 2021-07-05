package com.mr3y.thoughts.components.bottombar

import com.google.common.truth.Truth.assertThat
import com.mr3y.thoughts.components.bottombar.state.BottomBarState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class BottomBarStateTest {

    @Test(expected = IllegalArgumentException::class)
    fun `invalid num of tabs should throw an exception`() {
        val state = BottomBarState(2, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `invalid selected tab index should throw an exception`() {
        val state = BottomBarState(4, -1)
    }

    @Test
    fun `valid state arguments should initialize state instance successfully`() {
        val state = BottomBarState(3, 0)

        assertThat(state.tabsNumExcludingCurve).isEqualTo(2)
        assertThat(state.selectedTabIndex).isEqualTo(0)
    }

    @Test
    fun `update index with the same selected value shouldn't trigger recomposition`() {
        val state = BottomBarState(5, 2)
        val newSelectedIndex = 2

        state.updateSelectedIndex(newSelectedIndex)
        assertThat(state.selectedTabIndex).isEqualTo(2)
    }

    @Test
    fun `update index with new value should trigger recomposition`() {
        val state = BottomBarState(5, 2)
        val newSelectedIndex = 3

        state.updateSelectedIndex(newSelectedIndex)
        assertThat(state.selectedTabIndex).isEqualTo(3)
    }
}
