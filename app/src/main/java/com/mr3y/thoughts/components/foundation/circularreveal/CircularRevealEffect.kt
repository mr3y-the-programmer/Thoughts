package com.mr3y.thoughts.components.foundation.circularreveal

import android.view.Window
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mr3y.thoughts.ui.theme.LocalTheme
import com.mr3y.thoughts.ui.theme.ThoughtsTheme
import kotlinx.coroutines.launch
import kotlin.math.hypot

@Composable
fun CircularRevealScreen(
    window: Window? = null
) {
    val theme = LocalTheme.current
    var isLight by remember { mutableStateOf(theme == ThoughtsTheme.lightPalette) }
    CircularRevealLayout(
        modifier = Modifier.fillMaxSize(),
        isLight = isLight,
        window = window
    ) {
        Switch(
            checked = !isLight,
            modifier = Modifier
                .size(72.dp, 48.dp)
                .semantics {
                    contentDescription =
                        if (isLight) "Switch to dark theme" else "Switch to light theme"
                },
            onCheckedChange = { isLight = !isLight }
        )
    }
}

/**
 * A layout which clips out its content with a circular reveal effect
 * on changing theme from light to dark and vice versa, it also takes care of
 * changing window status bar, navigation bar colors
 */
@Composable
fun CircularRevealLayout(
    isLight: Boolean,
    modifier: Modifier = Modifier,
    window: Window? = null,
    content: @Composable () -> Unit
) {
    var radius by remember { mutableStateOf(0f) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = if (isLight) Color.White else Color.Black.copy(0.7f),
                    radius = radius,
                    center = Offset(size.width, 0f),
                )
            },
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(LocalTheme provides if (isLight) ThoughtsTheme.lightPalette else ThoughtsTheme.darkPalette) {
            content()
        }
    }
    val animatedRadius = remember { Animatable(0f) }
    val (width, height) = with(LocalConfiguration.current) {
        with(LocalDensity.current) { screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx() }
    }
    val maxRadiusPx = hypot(width, height)
    val systemBarsColor by animateColorAsState(targetValue = LocalTheme.current.primaryVariant)
    LaunchedEffect(isLight) {
        launch {
            animatedRadius.animateTo(maxRadiusPx, animationSpec = tween()) {
                radius = value
            }
            // reset the initial value after finishing animation
            animatedRadius.snapTo(0f)
        }
        launch {
            window?.statusBarColor = systemBarsColor.toArgb()
            window?.navigationBarColor = systemBarsColor.toArgb()
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun CircularRevealLayoutPreview() {
    ThoughtsTheme {
        CircularRevealScreen()
    }
}
