package com.xeniac.chillclub.core.ui.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xeniac.chillclub.core.ui.theme.BlackAlpha24

@Composable
fun CustomIconButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    size: Dp = 56.dp,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    shadowColorLight: Color = BlackAlpha24,
    shadowColorDark: Color = BlackAlpha24,
    shadowCornerRadius: Dp = 90.dp,
    shadowBlurRadius: Dp = 16.dp,
    shadowSpread: Dp = 4.dp,
    contentDescription: String? = null,
    iconSize: Dp = 28.dp,
    indication: Indication = ripple(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .customShadow(
                isSystemInDarkTheme = isSystemInDarkTheme,
                lightThemeShadowColor = shadowColorLight,
                darkThemeShadowColor = shadowColorDark,
                shadowCornerRadius = shadowCornerRadius,
                blurRadius = shadowBlurRadius,
                spread = shadowSpread
            )
            .clip(shape)
            .background(containerColor)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                indication = indication,
                interactionSource = interactionSource
            )
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(iconSize)
        )
    }
}