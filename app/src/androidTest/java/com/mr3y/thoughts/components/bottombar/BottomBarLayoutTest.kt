package com.mr3y.thoughts.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.karumi.shot.ScreenshotTest
import com.mr3y.thoughts.components.bottombar.state.BottomBarLayout
import com.mr3y.thoughts.components.bottombar.state.rememberBottomBarState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BottomBarLayoutTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun testAllItemsDisplayed_asExpected() {
        composeRule.setContent {
            val state = rememberBottomBarState(numberOfTabs = 5, selectedTabIndex = 2, stateScope = coroutineRule)
            BottomBarLayout(
                state = state,
                bottomBarItemsHeight = 60.dp,
                fab = { /*no-op for now*/ },
                curve = {
                    DockedFABCurve(
                        curveBackground = MaterialTheme.colors.surface,
                        modifier = Modifier
                            .weight(1.6f)
                            .background(Color.Transparent)
                    )
                },
                tab = {
                    Tab(
                        tabIcon = Icons.Outlined.Settings,
                        tint = Color.DarkGray.copy(0.7f),
                        modifier = Modifier
                            .weight(0.9f)
                            .clip(RectangleShape)
                            .background(MaterialTheme.colors.surface)
                    ) {
                    }
                }
            )
        }

        compareScreenshot(composeRule)
    }
}
