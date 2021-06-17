package com.mr3y.thoughts.components.foundation.recursion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

@Composable
fun rememberRecursionLoopState(config: LoopConfiguration.() -> Unit = {}): RecursionLoopState {
    return remember { RecursionLoopState(LoopConfiguration.apply { config() }) }
}

/**
 * manages the state of [RecursionLoop] to make it as stateless as possible
 */
class RecursionLoopState internal constructor(config: LoopConfiguration) {

    var iterationsNum by mutableStateOf(config.iterationsNum)
        private set

    private val _shapeScaleFactor = Animatable(config.initialShapeScaleFactor, Float.VectorConverter)
    val shapeScaleFactor: Float
        get() = _shapeScaleFactor.value

    private val _tickUpScaleFactor = Animatable(config.initialTickUpScaleFactor, Float.VectorConverter)
    val tickUpScaleFactor: Float
        get() = _tickUpScaleFactor.value

    private val _tickDownScaleFactor = Animatable(config.initialTickDownScaleFactor, Float.VectorConverter)
    val tickDownScaleFactor: Float
        get() = _tickDownScaleFactor.value

    /**
     * Start looping from the given frame in milliseconds
     */
    @Composable
    fun StartLooping(frameTime: State<Long>) {
        LaunchedEffect(frameTime.value) {
            iterationsNum += LoopConfiguration.DEFAULT_ITERATIONS_INTERVAL
            launch {
                _shapeScaleFactor.animateTo(_shapeScaleFactor.value - 0.00255f)
            }
            launch {
                _tickUpScaleFactor.animateTo(
                    2f,
                    spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                _tickUpScaleFactor.snapTo(1.0f) // reset to the default value
            }
            launch {
                _tickDownScaleFactor.animateTo(
                    0.5f,
                    spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
        }
    }
}
