package com.xeniac.chillclub.feature_settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xeniac.chillclub.core.ui.theme.Gray5
import com.xeniac.chillclub.core.ui.theme.Gray80

@Composable
fun SettingsHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 2.dp,
    shape: Shape = RoundedCornerShape(4.dp),
    color: Color = if (isSystemInDarkTheme()) Gray5 else Gray80,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .clip(shape)
            .background(color)
    )
}