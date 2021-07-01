package com.mr3y.thoughts.components.bottombar

import androidx.compose.animation.core.spring
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BottomBarStateTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test(expected = IllegalArgumentException::class)
    fun `invalid num of tabs should throw an exception`() {
        val state = BottomBarState(2, 0, coroutineRule)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `invalid selected tab index should throw an exception`() {
        val state = BottomBarState(4, -1, coroutineRule)
    }

    @Test
    fun `valid state arguments should initialize state instance successfully`() {
        val state = BottomBarState(3, 0, coroutineRule)

        assertThat(state.tabsNumExcludingCurve).isEqualTo(2)
        assertThat(state.selectedTabIndex).isEqualTo(0)
    }

    @Test
    fun `position curve for the first time shouldn't trigger animation`() {
        val state = BottomBarState(5, 2, coroutineRule)
        val newPos = 30f

        state.translateToNewXPosition(newPos, spring())
        assertThat(state.curveTranslationX).isEqualTo(0f)

        // TODO: assert that prevPos is Equal to newPos
        // val prevPos = state::class.java.getField("previousCurveTranslationX") as Float
        // assertThat(prevPos).isEqualTo(newPos)
    }
}
