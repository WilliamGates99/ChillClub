package com.xeniac.chillclub.feature_settings.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xeniac.chillclub.R
import com.xeniac.chillclub.core.presentation.common.ui.utils.getStatusBarHeightDp

@Composable
fun SettingsTopAppBar(
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surface,
    contentPadding: PaddingValues = PaddingValues(
        start = 24.dp,
        end = 24.dp,
        top = 28.dp + getStatusBarHeightDp(),
        bottom = 28.dp
    ),
    title: String = stringResource(id = R.string.settings_app_bar_title),
    titleFontSize: TextUnit = 32.sp,
    titleFontWeight: FontWeight = FontWeight.Black,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    backIcon: Painter = painterResource(id = R.drawable.ic_core_back),
    backIconContentDescription: String = stringResource(id = R.string.core_content_description_back),
    backIconSize: Dp = 28.dp,
    onBackClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 18.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(background)
            .padding(contentPadding)
    ) {
        Image(
            painter = backIcon,
            contentDescription = backIconContentDescription,
            modifier = Modifier
                .size(backIconSize)
                .clickable(
                    role = Role.Button,
                    onClick = onBackClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false)
                )
        )

        Text(
            text = title,
            fontSize = titleFontSize,
            fontWeight = titleFontWeight,
            color = titleColor
        )
    }
}