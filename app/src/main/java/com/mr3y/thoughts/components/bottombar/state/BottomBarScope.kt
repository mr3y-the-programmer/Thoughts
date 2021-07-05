package com.mr3y.thoughts.components.bottombar.state

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

@LayoutScopeMarker
@Immutable
interface BottomBarScope {

    /**
     * the weight of item within its container. weight can have different meanings based on context,
     * For example: for a bottom bar items where they are usually positioned horizontally in a row, weight
     * here can represent the width of the item, However in other items like FAB which is positioned as a sibling
     * to the bottom bar, weight in this context can represent the height.
     */
    @Stable
    fun Modifier.weight(@FloatRange(from = 0.1) weight: Float = 1.0f): Modifier

    /**
     * Explicitly override built-in graphicsLayer modifier,
     * but it delegates to the built-in graphicsLayer modifier under the hood
     */
    @Stable
    fun Modifier.graphicsLayer(block: GraphicsLayerScope.() -> Unit): Modifier
}

internal object BottomBarScopeImpl : BottomBarScope {

    @Stable
    override fun Modifier.weight(@FloatRange(from = 0.1) weight: Float): Modifier {
        require(weight > 0.0) { "invalid weight $weight; must be greater than zero" }
        return this.then(
            BottomBarItemWeight(weight)
        )
    }

    @Stable
    override fun Modifier.graphicsLayer(block: GraphicsLayerScope.() -> Unit): Modifier {
        return this.then(
            ItemGraphicsLayer(block)
        )
    }
}

private data class BottomBarItemsData(
    var weightFactor: Float = 0f,
    var graphicsLayer: GraphicsLayerScope.() -> Unit = {}
)

// TODO: should this be an inline class?
private data class BottomBarItemWeight(val weight: Float) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = ((parentData as? BottomBarItemsData) ?: BottomBarItemsData()).apply {
        this.weightFactor = weight
    }
}

private data class ItemGraphicsLayer(val block: GraphicsLayerScope.() -> Unit) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = ((parentData as? BottomBarItemsData) ?: BottomBarItemsData()).apply {
        this.graphicsLayer = block
    }
}

internal val Measurable.weight: Float
    get() = (parentData as BottomBarItemsData).weightFactor

internal val Measurable.graphicsLayer
    get() = (parentData as BottomBarItemsData).graphicsLayer
