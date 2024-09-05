package com.xeniac.chillclub.core.ui.theme.utils

import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private var Handler: EdgeToEdgeHandler? = null

@JvmName(name = "enableEdgeToEdgeWindow")
fun ComponentActivity.enableEdgeToEdgeWindow() {
    WindowCompat.setDecorFitsSystemWindows(
        /* window = */ window,
        /* decorFitsSystemWindows = */ false
    )
}

@JvmName(name = "EnableEdgeToEdgeWindow")
@JvmOverloads
@Composable
fun EnableEdgeToEdgeWindow(
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            val edgeToEdgeHandlerImpl = Handler ?: when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> EdgeToEdgeApi30() // Android 11 and above (30+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> EdgeToEdgeHandlerApi29() // Android 10 and above (29+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> EdgeToEdgeHandlerApi28() // Android 9 and above (28+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> EdgeToEdgeHandlerApi26() // Android 8.0 and above (26+)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> EdgeToEdgeHandlerApi23() // Android 6.0 and above (23+)
                else -> EdgeToEdgeHandlerApi21() // Android 5.0 and above (21+)
            }.also { Handler = it }

            edgeToEdgeHandlerImpl.setUp(window, isDarkTheme)
            edgeToEdgeHandlerImpl.adjustLayoutInDisplayCutoutMode(window)

            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }
}

private interface EdgeToEdgeHandler {

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

private open class EdgeToEdgeHandlerBaseImpl : EdgeToEdgeHandler {

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

private class EdgeToEdgeHandlerApi21 : EdgeToEdgeHandlerBaseImpl() {

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
private class EdgeToEdgeHandlerApi23 : EdgeToEdgeHandlerBaseImpl() {

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
private open class EdgeToEdgeHandlerApi26 : EdgeToEdgeHandlerBaseImpl() {

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
private open class EdgeToEdgeHandlerApi28 : EdgeToEdgeHandlerApi26() {

    @DoNotInline
    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}

@RequiresApi(29)
private open class EdgeToEdgeHandlerApi29 : EdgeToEdgeHandlerApi28() {

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
private class EdgeToEdgeApi30 : EdgeToEdgeHandlerApi29() {

    @DoNotInline
    override fun adjustLayoutInDisplayCutoutMode(window: Window) {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
    }
}