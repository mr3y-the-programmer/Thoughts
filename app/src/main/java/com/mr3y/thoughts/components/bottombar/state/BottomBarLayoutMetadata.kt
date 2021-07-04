package com.mr3y.thoughts.components.bottombar.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.MeasureScope

/**
 * provides metadata about specific Layout measurements such as: items size,
 * position...etc that is not available before measuring phase
 */
internal sealed interface BottomBarLayoutMetadata {
    var tabWidth: Int

    var tabHeight: Int

    var curveWidth: Int

    var curveHeight: Int
}

internal object BottomBarLayoutMetaDataImpl : BottomBarLayoutMetadata {
    override var tabWidth: Int = 0

    override var tabHeight: Int = 0

    override var curveWidth: Int = 0

    override var curveHeight: Int = 0
}

internal var bottomBarLayoutMetadata: BottomBarLayoutMetadata by mutableStateOf(BottomBarLayoutMetaDataImpl)
    private set

internal fun MeasureScope.fillLayoutMetadata(block: BottomBarLayoutMetadata.() -> Unit) {
    val metadata = BottomBarLayoutMetaDataImpl
    metadata.apply { block() }
    bottomBarLayoutMetadata = metadata
}
