package com.mr3y.thoughts.components.bottombar.state

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

@LayoutScopeMarker
@Immutable
interface BottomBarScope {

    @Stable
    fun Modifier.weight(@FloatRange(from = 0.1) weight: Float = 1.0f): Modifier
}

internal object BottomBarScopeImpl : BottomBarScope {

    @Stable
    override fun Modifier.weight(@FloatRange(from = 0.1) weight: Float): Modifier {
        require(weight > 0.0) { "invalid weight $weight; must be greater than zero" }
        return this.then(
            BottomBarItemWeight(weight)
        )
    }
}

// TODO: should this be an inline class?
data class BottomBarItemWeight(val weight: Float) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any = this@BottomBarItemWeight
}

internal val Measurable.weight: Float
    get() = (parentData as BottomBarItemWeight).weight
