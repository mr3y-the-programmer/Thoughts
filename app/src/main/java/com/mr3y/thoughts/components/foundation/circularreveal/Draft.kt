package com.mr3y.thoughts.components.foundation.circularreveal

// This file contains draft code to support changing system bars colors in CircularRevealLayout when changing theme

/*val LocalWindow = staticCompositionLocalOf<Window> {
    error(
        "No Window instance is provided, Please make sure " +
            "you've provided one within CompositionLocalProvider{...} block in your activity or fragment"
    )
}*/

// Analogous to animating radius (a.k.a animatedRadius)
/*
val systemBarsColor = if (isLight) Color.White else Color.Black.copy(0.7f)
val animatedSystemBarsColor = remember { ColorAnimatable(systemBarsColor) }
val window = LocalWindow.current
*/

// Add this in LaunchedEffect block
/*launch {
    animatedSystemBarsColor.animateTo(systemBarsColor.invert(), animationSpec = tween(durationMillis = 200))
    window.statusBarColor = animatedSystemBarsColor.value.toArgb()
    window.navigationBarColor = animatedSystemBarsColor.value.toArgb()
}*/

// private fun Color.invert() = if (this == Color.White) Color.Black.copy(0.7f) else this
