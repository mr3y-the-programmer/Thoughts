package com.mr3y.thoughts.components.bottombar

import com.google.common.truth.Truth.assertThat
import com.mr3y.thoughts.components.bottombar.state.Ratio
import com.mr3y.thoughts.components.bottombar.state.mapToCorrespondingIndex
import com.mr3y.thoughts.components.bottombar.state.partitionByRatio
import org.junit.Test

class UtilsTest {

    @Test
    fun `given a ratio, check the produced parts are divided correctly`() {
        val (firstPart, secondPart) = 60.partitionByRatio(Ratio(2f, 3f))

        assertThat(firstPart).isEqualTo(24)
        assertThat(secondPart).isEqualTo(36)
    }

    @Test
    fun `given incorrect indices & selected index, correct and return the new indices`() {
        val incorrect = listOf(0, 1, 2, 3)
        val correct = incorrect.map { it.mapToCorrespondingIndex(2) }
        assertThat(correct).isEqualTo(listOf(0, 1, 3, 4))
    }
}
