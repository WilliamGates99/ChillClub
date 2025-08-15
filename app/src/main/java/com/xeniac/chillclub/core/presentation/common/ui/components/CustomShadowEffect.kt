package com.xeniac.chillclub.core.presentation.common.ui.components

import android.graphics.BlurMaskFilter
import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xeniac.chillclub.core.presentation.common.ui.theme.Black
import com.xeniac.chillclub.core.presentation.common.ui.theme.White

fun Modifier.customShadow(
    isSystemInDarkTheme: Boolean,
    lightThemeShadowColor: Color = Black,
    darkThemeShadowColor: Color = White,
    shadowCornerRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    spread: Dp = 0f.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
) = drawBehind {
    val shadowColor = if (isSystemInDarkTheme) darkThemeShadowColor else lightThemeShadowColor

    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()

        // on android 9 and below, blur radius does not work, hence the spread must be set to 0px
        val spreadPixel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            spread.toPx()
        } else 0f.dp.toPx()

        val leftPixel = (0f - spreadPixel) + offsetX.toPx()
        val topPixel = (0f - spreadPixel) + offsetY.toPx()
        val rightPixel = (this.size.width + spreadPixel)
        val bottomPixel = (this.size.height + spreadPixel)

        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = BlurMaskFilter(
                /* radius = */ blurRadius.toPx(),
                /* style = */ BlurMaskFilter.Blur.NORMAL
            )
        }

        frameworkPaint.color = shadowColor.toArgb()

        canvas.drawRoundRect(
            left = leftPixel,
            top = topPixel,
            right = rightPixel,
            bottom = bottomPixel,
            radiusX = shadowCornerRadius.toPx(),
            radiusY = shadowCornerRadius.toPx(),
            paint = paint
        )
    }
}

fun Modifier.customOvalShadow(
    isSystemInDarkTheme: Boolean = false,
    lightThemeShadowColor: Color = Black,
    darkThemeShadowColor: Color = White,
    blurRadius: Dp = 0.dp,
    spread: Dp = 0f.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
) = drawBehind {
    val shadowColor = if (isSystemInDarkTheme) darkThemeShadowColor else lightThemeShadowColor

    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()

        // on android 9 and below, blur radius does not work, hence the spread must be set to 0px
        val spreadPixel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spread.toPx()
        } else 0f.dp.toPx()

        val leftPixel = (0f - spreadPixel) + offsetX.toPx()
        val topPixel = (0f - spreadPixel) + offsetY.toPx()
        val rightPixel = (this.size.width + spreadPixel)
        val bottomPixel = (this.size.height + spreadPixel)

        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = BlurMaskFilter(
                /* radius = */ blurRadius.toPx(),
                /* style = */ BlurMaskFilter.Blur.NORMAL
            )
        }

        frameworkPaint.color = shadowColor.toArgb()

        canvas.drawOval(
            rect = Rect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel
            ),
            paint = paint
        )
    }
}