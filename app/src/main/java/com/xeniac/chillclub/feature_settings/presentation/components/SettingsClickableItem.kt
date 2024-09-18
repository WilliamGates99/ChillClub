package com.xeniac.chillclub.feature_settings.presentation.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsClickableItem(
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier,
    rowPadding: PaddingValues = PaddingValues(
        horizontal = 28.dp,
        vertical = 20.dp
    ),
    iconSize: Dp = 20.dp,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.ExtraBold,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleMaxLines: Int = 1,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                role = Role.Button
            )
            .padding(rowPadding)
    ) {
        Icon(
            painter = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )

        Text(
            text = title,
            fontSize = titleFontSize,
            fontWeight = titleFontWeight,
            color = titleColor,
            maxLines = titleMaxLines,
            modifier = Modifier
                .weight(1f)
                .basicMarquee()
        )
    }
}