package com.mr3y.thoughts.components.bottombar

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsTest {

    @Test
    fun `given a ratio, check the produced parts are divided correctly`() {
        val (firstPart, secondPart) = 60.partitionByRatio(Ratio(2f, 3f))

        assertThat(firstPart).isEqualTo(24)
        assertThat(secondPart).isEqualTo(36)
    }
}
