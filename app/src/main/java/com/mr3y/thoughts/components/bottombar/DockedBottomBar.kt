package com.mr3y.thoughts.components.bottombar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mr3y.thoughts.components.bottombar.state.BottomBarLayout
import com.mr3y.thoughts.components.bottombar.state.bottomBarLayoutMetadata
import com.mr3y.thoughts.components.bottombar.state.rememberBottomBarState
import com.mr3y.thoughts.ui.theme.ThoughtsTheme
import kotlinx.coroutines.launch

// Copied from androidx.compose.material
private val AppBarHeight = 56.dp

/**
 * BottomBar that displays currently selected Item as a docked FAB
 */
@Composable
fun DockedBottomBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffff6f00))
    ) {
        val coroutineScope = rememberCoroutineScope()
        val state = rememberBottomBarState(numberOfTabs = 5, selectedTabIndex = 2)
        val curveAnimSpec: AnimationSpec<Int> = spring(0.85f)
        // TODO: convert those to transition state
        var currentCurveTranslationX by mutableStateOf(0)
        val animatedTranslationX = remember { Animatable(0, Int.VectorConverter) }
        var fabTranslationY by mutableStateOf(0)
        val animatedTranslationY = remember { Animatable(0, Int.VectorConverter) }
        val fabIconAlpha = remember { Animatable(0.7f, Float.VectorConverter) }
        BottomBarLayout(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .requiredHeight(AppBarHeight * 1.5f)
                .fillMaxWidth()
                .background(Color.Transparent),
            bottomBarItemsHeight = AppBarHeight,
            state = state,
            fab = { index ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .weight(0.7f)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colors.surface)
                        .graphicsLayer {
                            translationX = if (currentCurveTranslationX != 0) {
                                currentCurveTranslationX
                            } else {
                                bottomBarLayoutMetadata.tabWidth * state.selectedTabIndex
                            }.toFloat()
                            translationY = fabTranslationY.toFloat()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(48.dp, 48.dp)
                            .padding(4.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            imageVector = iconFromIndex(index),
                            tint = Color.DarkGray.copy(fabIconAlpha.value),
                            contentDescription = "TODO:",
                        )
                    }
                }
            },
            curve = {
                DockedFABCurve(
                    curveBackground = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .weight(1.6f)
                        .aspectRatio(2.15f)
                        .background(Color.Transparent)
                        .graphicsLayer {
                            translationX = if (currentCurveTranslationX != 0) {
                                currentCurveTranslationX
                            } else {
                                bottomBarLayoutMetadata.tabWidth * state.selectedTabIndex
                            }.toFloat()
                        }
                )
            },
            tab = { tabIndex ->
                Tab(
                    tabIcon = iconFromIndex(tabIndex),
                    tint = Color.DarkGray.copy(0.7f),
                    modifier = Modifier
                        .weight(0.9f)
                        .clip(RectangleShape)
                        .background(MaterialTheme.colors.surface)
                ) {
                    state.updateSelectedIndex(tabIndex)
                    val newPosition = (bottomBarLayoutMetadata.tabWidth * tabIndex)
                    coroutineScope.launch {
                        animatedTranslationX.animateTo(newPosition, curveAnimSpec) {
                            currentCurveTranslationX = value
                        }
                    }
                    coroutineScope.launch {
                        animatedTranslationY.animateTo(bottomBarLayoutMetadata.fabHeight, tween(durationMillis = 100)) {
                            fabTranslationY = value
                        }
                        animatedTranslationY.animateTo(0, tween()) {
                            fabTranslationY = value
                        }
                    }
                    coroutineScope.launch {
                        fabIconAlpha.animateTo(0f, tween(durationMillis = 200))
                        fabIconAlpha.animateTo(0.7f, tween())
                    }
                }
            }
        )
    }
}

@Composable
internal fun DockedFABCurve(
    curveBackground: Color,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val (widthInPx, heightInPx) = with(LocalDensity.current) {
            maxWidth.toPx() to maxHeight.toPx()
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(widthInPx, 0f)
                // Don't call moveTo() again to force path
                // not to start a new subpath which would
                // result in unexpected effect
                relativeLineTo(0f, heightInPx)
                relativeLineTo(-widthInPx, 0f)
                relativeLineTo(0f, -heightInPx)
                relativeCubicTo(
                    dx3 = widthInPx * 0.5f, // end point
                    dy3 = heightInPx * 0.8f, // end point
                    // control points
                    dx1 = widthInPx * 0.2f,
                    dy1 = 0f,
                    dx2 = widthInPx * 0.25f,
                    dy2 = heightInPx * 0.8f
                )
                relativeCubicTo(
                    dx3 = widthInPx * 0.5f,
                    dy3 = -heightInPx * 0.8f,
                    dx1 = widthInPx * 0.25f,
                    dy1 = 0f,
                    dx2 = widthInPx * 0.3f,
                    dy2 = -heightInPx * 0.8f
                )
            }

            clipPath(
                path = path,
                clipOp = ClipOp.Intersect
            ) {
                drawPath(
                    path,
                    Color.Transparent,
                    style = Stroke(width = 1f, cap = StrokeCap.Round)
                )

                drawRect(
                    color = curveBackground,
                    size = Size(widthInPx, heightInPx)
                )
            }
        }
    }
}

@Composable
internal fun Tab(
    tabIcon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.padding(4.dp)
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .indication(MutableInteractionSource(), null),
            onClick = { onClick() },
            interactionSource = MutableInteractionSource()
        ) {
            Icon(
                imageVector = tabIcon,
                tint = tint,
                contentDescription = "TODO:",
            )
        }
    }
}

private fun iconFromIndex(tabIndex: Int): ImageVector {
    return when (tabIndex) {
        0 -> Icons.Outlined.AccountCircle
        1 -> Icons.Outlined.Search
        2 -> Icons.Outlined.Home
        3 -> Icons.Outlined.Email
        4 -> Icons.Outlined.Settings
        else -> throw IllegalArgumentException("BottomBar shouldn't contain more than 5 tabs")
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    ThoughtsTheme {
        DockedBottomBar()
    }
}
