package com.mr3y.thoughts.components.bottombar

internal typealias Ratio = Pair<Float, Float>

/**
 * Divide a part into two parts satisfying the given ratio
 *
 * For example: given x=60 & we need to divide it by a ratio of 2:3 would result in 24, 36 respectively
 */
internal fun Int.partitionByRatio(ratio: Ratio): Pair<Float, Float> {
    val sum = ratio.first + ratio.second
    return Pair((ratio.first * this) / sum, (ratio.second * this) / sum)
}
