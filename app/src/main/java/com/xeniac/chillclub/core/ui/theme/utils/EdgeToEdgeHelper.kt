package com.xeniac.chillclub.core.ui.theme.utils

import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private var Impl: EdgeToEdgeImpl? = null

@JvmName(name = "EnableEdgeToEdge")
@JvmOverloads
@Composable
fun EnableEdgeToEdge(
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(
                /* window = */ window,
                /* decorFitsSystemWindows = */ false
            )

            val edgeToEdgeImpl = Impl ?: when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> EdgeToEdgeApi30() // Android 11 and above (30+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> EdgeToEdgeApi29() // Android 10 and above (29+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> EdgeToEdgeApi28() // Android 9 and above (28+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> EdgeToEdgeApi26() // Android 8.0 and above (26+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> EdgeToEdgeApi23() // Android 6.0 and above (23+)
                else -> EdgeToEdgeApi21() // Android 5.0 and above (21+)
            }.also { Impl = it }

            edgeToEdgeImpl.setUp(window, isDarkTheme)
            edgeToEdgeImpl.adjustLayoutInDisplayCutoutMode(window)

            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }
}

private interface EdgeToEdgeImpl {

    val statusBarColorV21: Color
    val navigationBarColorV23: Color
    val navigationBarColorV26Light: Color
    val navigationBarColorV26Dark: Color

    fun setUp(
        window: Window,
        isDarkTheme: Boolean
    )

    fun adjustLayoutInDisplayCutoutMode(window: Window)
}

private open class EdgeToEdgeBase : EdgeToEdgeImpl {

    override val statusBarColorV21: Color
        get() = Color(0x26444444)

    override val navigationBarColorV23: Color
        get() = Color(0x801B1B1B)

    override val navigationBarColorV26Light: Color
        get() = Color(0xE6FFFFFF)

    override val navigationBarColorV26Dark: Color
        get() = Color(0x801B1B1B)

    override fun setUp(
        window: Window,
        isDarkTheme: Boolean
    ) {
        // No edge-to-edge before SDK 21 (Android 5.0).
    }

    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        // No display cutout before SDK 28 (Android 9).
    }
}

private class EdgeToEdgeApi21 : EdgeToEdgeBase() {

    @Suppress("DEPRECATION")
    @DoNotInline
    override fun setUp(
        window: Window,
        isDarkTheme: Boolean
    ) {
        window.statusBarColor = if (isDarkTheme) {
            Color.Transparent.toArgb()
        } else statusBarColorV21.toArgb()
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
}

@RequiresApi(23)
private class EdgeToEdgeApi23 : EdgeToEdgeBase() {

    @Suppress("DEPRECATION")
    @DoNotInline
    override fun setUp(
        window: Window,
        isDarkTheme: Boolean
    ) {
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = navigationBarColorV23.toArgb()
    }
}

@RequiresApi(26)
private open class EdgeToEdgeApi26 : EdgeToEdgeBase() {

    @Suppress("DEPRECATION")
    @DoNotInline
    override fun setUp(
        window: Window,
        isDarkTheme: Boolean
    ) {
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = if (isDarkTheme) {
            navigationBarColorV26Dark.toArgb()
        } else navigationBarColorV26Light.toArgb()
    }
}

@RequiresApi(28)
private open class EdgeToEdgeApi28 : EdgeToEdgeApi26() {

    @DoNotInline
    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

@RequiresApi(29)
private open class EdgeToEdgeApi29 : EdgeToEdgeApi28() {

    @Suppress("DEPRECATION")
    @DoNotInline
    override fun setUp(
        window: Window,
        isDarkTheme: Boolean
    ) {
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        window.isNavigationBarContrastEnforced = true
    }
}

@RequiresApi(30)
private class EdgeToEdgeApi30 : EdgeToEdgeApi29() {

    @DoNotInline
    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
    }
}