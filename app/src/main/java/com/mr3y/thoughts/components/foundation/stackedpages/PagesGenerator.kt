package com.mr3y.thoughts.components.foundation.stackedpages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class PagesGenerator {
    var pages by mutableStateOf(2)
        private set

    fun addNewPage() {
        pages++
    }
}
