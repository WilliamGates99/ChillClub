package com.xeniac.chillclub.feature_settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.core.ui.components.addTestTag
import com.xeniac.chillclub.core.ui.theme.Gray60
import com.xeniac.chillclub.core.ui.theme.Gray80

@Composable
fun SettingsSwitchRow(
    icon: Painter,
    title: String,
    isChecked: Boolean?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(52.dp),
    border: BorderStroke = BorderStroke(
        width = 2.dp,
        color = Gray80
    ),
    rowPadding: PaddingValues = PaddingValues(
        horizontal = 20.dp,
        vertical = 20.dp
    ),
    iconSize: Dp = 20.dp,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    titleFontSize: TextUnit = 16.sp,
    titleFontWeight: FontWeight = FontWeight.ExtraBold,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleMaxLines: Int = 1,
    description: String? = null,
    descriptionFontSize: TextUnit = 12.sp,
    descriptionFontWeight: FontWeight = FontWeight.Normal,
    descriptionTextAlign: TextAlign = TextAlign.Start,
    descriptionColor: Color = Gray60,
    switchColors: SwitchColors = SwitchDefaults.colors().copy(
        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceBright,
        uncheckedBorderColor = Color.Transparent,
        disabledUncheckedTrackColor = MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.38f),
        disabledUncheckedBorderColor = Color.Transparent
    ),
    testTag: String? = null,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .border(
                    border = border,
                    shape = shape
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

            Switch(
                enabled = isChecked != null,
                checked = isChecked ?: false,
                onCheckedChange = onCheckedChange,
                colors = switchColors,
                modifier = Modifier
                    .height(32.dp)
                    .addTestTag(tag = testTag)
            )
        }

        description?.let { description ->
            Text(
                text = description,
                fontSize = descriptionFontSize,
                fontWeight = descriptionFontWeight,
                textAlign = descriptionTextAlign,
                color = descriptionColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
        }
    }
}