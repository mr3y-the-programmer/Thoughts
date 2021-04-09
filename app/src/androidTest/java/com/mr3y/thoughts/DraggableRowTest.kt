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
import com.mr3y.thoughts.components.foundation.DraggableRow
import com.mr3y.thoughts.components.foundation.rememberDraggableRowState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DraggableRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val state = rememberDraggableRowState(itemsNum = 20)
            DraggableRow(
                state = state,
                itemSpacing = 24.dp,
                modifier = Modifier.semantics { contentDescription = "Draggable Row" }
            ) {
                Button(onClick = { /*TODO*/ }, modifier = Modifier.size(96.dp, 48.dp)) {
                    Text(text = "Button $currentItemIndex")
                }
            }
        }
    }

    @Test
    fun assert_row_isDisplayed_and_scrollable() {
        composeTestRule.onNodeWithContentDescription("Draggable Row").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Draggable Row").onChildren().assertCountEquals(20)

        composeTestRule.onNodeWithText("Button 19").assertIsNotDisplayed()

        // scroll
        composeTestRule.onNode(hasText("Button 19")).performScrollTo()

        // assert is displayed
        composeTestRule.onNodeWithText("Button 19").assertIsDisplayed()
    }

    @Test
    fun onDrag_assert_row_updates_items_positions() {
        composeTestRule.onNode(hasText("Button 1")).assertIsDisplayed()

        composeTestRule.onNode(hasText("Button 1")).performGesture { /* drag down */ }

        // assert button 2 is displayed before button 1
    }
}
