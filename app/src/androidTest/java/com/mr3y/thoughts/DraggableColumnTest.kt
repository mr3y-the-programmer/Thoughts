package com.mr3y.thoughts

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mr3y.thoughts.components.foundation.DraggableColumn
import com.mr3y.thoughts.components.foundation.rememberDraggableColumnState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DraggableColumnTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val state = rememberDraggableColumnState(itemsNum = 20)
            DraggableColumn(
                state = state,
                itemSpacing = 24.dp,
                modifier = Modifier.semantics { contentDescription = "Draggable Column" }
            ) {
                Button(onClick = { /*TODO*/ }, modifier = Modifier.size(96.dp, 48.dp)) {
                    Text(text = "Button $currentItemIndex")
                }
            }
        }
    }

    @Test
    fun assert_column_isDisplayed_and_scrollable() {
        composeTestRule.onNodeWithContentDescription("Draggable Column").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Draggable Column").onChildren().assertCountEquals(20)

        composeTestRule.onNodeWithText("Button 19").assertIsNotDisplayed()

        // scroll
        composeTestRule.onNode(hasText("Button 19")).performScrollTo()

        // assert is displayed
        composeTestRule.onNodeWithText("Button 19").assertIsDisplayed()
    }

    @Test
    fun onDrag_assert_column_updates_items_positions() {
        composeTestRule.onNode(hasText("Button 1")).assertIsDisplayed()

        composeTestRule.onNode(hasText("Button 1")).performGesture { /* drag down */ }

        // assert button 2 is displayed before button 1
    }
}
